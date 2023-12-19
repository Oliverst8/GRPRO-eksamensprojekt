package main;

import java.awt.Color;

import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Rabbit extends NestAnimal {
    private Burrow burrow; //The burrow the rabbit object belongs to

    /**
     * Initilises the food to the bunny can eat to plant and fruits
     * Initialises inBurrow to false
     */
    public Rabbit() {
        super(0);
        burrow = null;
        initialize();
    }

    /**
     *
     * @param age initialises age of the rabbit
     * @param burrow initialises the burrow that the rabbit belongs to
     * @param inBurrow parameter to determine if the rabbit should be initialised inside of the burrow
     * if inburrow is true, calls setInNest(true) and adds rabbit to a member of the burrow
     */
    public Rabbit(int age, Burrow burrow, boolean inBurrow) {
        super(0);
        setBurrow(burrow);
        initialize();
        this.age = age;

        if(inBurrow) {
            setInNest(true);
            burrow.addMember(this);
        }
    }

    @Override
    protected void setupCanEat() {
        addCanEat(Grass.class);
    }

    @Override
    public void die(World world) {
        // If the rabbit is in a burrow, it exits the burrow. (This is to be able to spawn a carcass)
        if(isInNest()) exitNest(world);
        super.die(world);

        // Removes the rabbit from the burrow it belongs to.
        if(burrow != null) burrow.removeMember(this);
    }

    @Override
    public String getType() {
        return "rabbit";
    }

    @Override
    public Color getColor() {
        return Color.red;
    }

    @Override
    protected void hungryBehavior(World world) {
        // If there are animals that can eat the rabbit nearby, it moves away from them.
        if (world.getSurroundingTiles(world.getLocation(this)).size() > world.getEmptySurroundingTiles(world.getLocation(this)).size()) {
            for (Location location : world.getSurroundingTiles(world.getLocation(this))) {

                if (world.isTileEmpty(location)) continue;

                Object object = world.getTile(location);

                if (object instanceof Animal) {
                    if (((Animal) object).canIEat(this.getClass())) {
                        moveAwayFrom(world, location);
                        return;
                    }
                }
            }
        }

        // Find the closest pray.
        Organism closestPrey = findPrey(world, 4);

        // If there is a prey nearby, it hunts it. Else it wanders around.
        if (closestPrey != null) {
            huntPrey(world, closestPrey);
        } else {
            wander(world);
        }
    }

    @Override
    public Nest getNest() {
        return burrow;
    }

    @Override
    protected void noNestBehavior(World world) {
        // If rabbit should dig and its energy is over 25, it digs a burrow.
        if (shouldRabbitDig(world) && getEnergy() > 25) {
            dig(world);
        } else { // Joins a nearby burrow that has a entry from withing a 5 radius.
            setBurrow(((RabbitHole) findNearestPrey(world, 5, RabbitHole.class)).getBurrow());
            goToNest(world);
        }
    }

    @Override
    protected void moveTowardsNest(World world) {
        Location nearestEntry = burrow.findNearestEntry(world, world.getCurrentLocation());

        // If the rabbit is not at the entry, it moves towards it.
        if(Utility.distance(world.getLocation(this), nearestEntry) >= 2) {
            moveTowards(world, nearestEntry);
        } else if(Utility.distance(world.getLocation(this), nearestEntry) < 2) { // If the rabbit is at the entry, it enters the burrow.
            enterNest(world);
        }
    }

    @Override
    protected void produceOffSpring(World world) {
        // The rabbit belongs to the same burrow as the parents. And is spawned inside of the burrow.
        ObjectFactory.generateOffMap(world, "Rabbit", 0, burrow, true);
    }

    @Override
    protected void inNestBehavior(World world) {
        // If the rabbit can reproduce, it does so.
        if(reproduceBehavior(world)) return;

        // If the rabbit has more than 60 energy, it expands the burrow with a new entry.
        if(getEnergy() > 60) {
            expandBurrow(world);
            return;
        }

        // If the rabbit is hungry, it exits the burrow to hunt.
        if(getHunger() < 100) exitNest(world);
    }

    @Override
    protected Location getExitLocation(World world) {
        Set<RabbitHole> entries = burrow.getEntries();
        Location freeLocation = null;

        for(Hole entry : entries) {
            List<Location> emptyLocations = new ArrayList<>();
            
            // If the entry is empty, it adds it to the list of empty locations.
            if(world.isTileEmpty(entry.getLocation(world))) {
                emptyLocations.add(entry.getLocation(world)); 
            }

            // Adds all empty surrounding tiles to the list of empty locations.
            emptyLocations.addAll(world.getEmptySurroundingTiles(entry.getLocation(world)));

            // If there are no empty locations, continue to the next entry.
            if(emptyLocations.isEmpty()) continue;

            // Picks a random location from the list of empty locations.
            int random = new Random().nextInt(emptyLocations.size());
            freeLocation = emptyLocations.get(random);

            break; //breaks loop if freeLocation is found.
        }

        return freeLocation;
    }

    /**
     * Called in constructor
     * Initializes alot of values for the rabbit, such as:
     * Adultage, maxenergy, sets energy as maxenergy, strength, maxhealth, health set as maxhealth
     *
     */
    private void initialize() {
        adultAge = 3;
        maxEnergy = 100;
        energy = maxEnergy;
        damage = 100;
        maxHealth = 100;
        health = maxHealth;
    }

    /**
     * Determines if rabbit should dig or not, by checking if there are any nearby entries and if so it joins the burrow that the hole belongs to instead
     * Returns true if there are no burrowentries from within a distance of 5
     * @param world
     * @return
     */
    private boolean shouldRabbitDig(World world) {
        RabbitHole nearestBurrowEntry = (RabbitHole) findNearestPrey(world, 5, RabbitHole.class);
        if(nearestBurrowEntry == null) return true;

        return !(Utility.distance(world.getLocation(this), world.getLocation(nearestBurrowEntry)) * 5 > 25);
    }

    /**
     * Throws Error.IllegalOperationException if dig is called when the bunny already has a burrow
     * If The bunny has at least 25 energy:
     * - calls makeBurrow()
     */
    private void dig(World world) {
        if(getEnergy()-25 > 0 ) {
            makeBurrow(world);
            enterNest(world);
        }
    }

    /**
     * - Makes a burrow at the current location
     *      *      * - Initialises burrow variable to newly created burrow
     *      *      * - Subtracts 25 energy
     */
    private void makeBurrow(World world) {
        setBurrow((Burrow) ObjectFactory.generateOnMap(world,"Burrow", world.getCurrentLocation()));
        removeEnergy(25);
    }

    /**
     * Throws Error.IllegalOperationException if the bunny has no burrow
     * If the bunny does not have 50 energy return;
     * - makes entrance to burrow
     * - removes 50 energy
     */
    private void expandBurrow(World world) {
        if(getEnergy()-50 > 0) {
            Location location = Utility.findNonBlockingEmptyLocation(world);
            burrow.addEntry(world, location);
            removeEnergy(50);
        }
    }

    /**
     * Throws IllegalArgumentException if burrow is null
     * Throws Error.IllegalOperationException if the bunny has a burrow already
     * Initializes the burrow to the argument
     * @param burrow The burrow which the bunny should make its own
     */
    private void setBurrow(Burrow burrow) {
        this.burrow = burrow;
    }
}
