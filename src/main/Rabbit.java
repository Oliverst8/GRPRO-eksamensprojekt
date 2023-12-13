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

    /**
     * adds grass to the list of things that the rabbit can eat.
     */
    @Override
    protected void setupCanEat() {
        addCanEat(Grass.class);
    }

    /**
     * Kills the rabbit and the removes it from the list of members of the burrow
     * @param world current world
     */
    @Override
    public void die(World world) {
        if(isInNest()) exitNest(world);
        super.die(world);

        if(burrow != null) burrow.removeMember(this);
    }

    /**
     * returns the type that the rabbit is as a string
     * @return "rabbit"
     */
    @Override
    public String getType() {
        return "rabbit";
    }

    /**
     * @return the color red
     */
    @Override
    public Color getColor() {
        return Color.red;
    }

    /**
     * checks if there are objects in the rabbits surroundingTiles
     * if so, gets object and checks if the rabbit can be eaten by the object nearby, and if it can it moves away
     * else it check if nearest grass is within a 4 radius distance
     * if so it hunts it down and consumes it
     * else it moves around randomly
     * @param world
     */
    @Override
    protected void hungryBehavior(World world) {
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

        Organism closestPrey = findPrey(world, 4);

        if (closestPrey != null) {
            huntPrey(world, closestPrey);
        } else {
            wander(world);
        }
    }

    /**
     * @return the nest/burrow that the rabbit belongs to
     */
    public Nest getNest() {
        return burrow;
    }

    /**
     * Is called if rabbit has no nest, if rabbit should dig and its energy is over 25, it digs a burrow
     * else it joins a nearby burrow that has a entry from withing a 5 radius distance of its current location and goes to it.
     * @param world Takes world as a parameter
     */
    protected void noNestBehavior(World world) {
        if (shouldRabbitDig(world) && getEnergy() > 25) {
            dig(world);
        } else {
            setBurrow(((RabbitHole) findNearestPrey(world, 5, RabbitHole.class)).getBurrow());
            goToNest(world);
        }
    }

    /**
     * Calls findNearestEntry from the rabbits location to get the location of the nearest entry of the burrow
     * if the distance is more or equal to 2 it moves towards the entry
     * if it is below 2 it enters the burrow
     * @param world
     */
    protected void moveTowardsNest(World world) {
        Location nearestEntry = burrow.findNearestEntry(world, world.getCurrentLocation());
        if(Helper.distance(world.getLocation(this), nearestEntry) >= 2) moveTowards(world, nearestEntry);
        if(Helper.distance(world.getLocation(this), nearestEntry) < 2) enterNest(world);
    }

    /**
     * Is called when rabbit reproduces
     * Generates a new rabbit belonging to the same burrow as the rabbits that contributed to its creation
     * Spawns it inside of the burrow and initialises the age to 0
     * @param world
     */
    protected void produceOffSpring(World world) {
        ObjectFactory.generateOffMap(world, "Rabbit", 0, burrow, true);
    }

    /**
     * Behavior made for rabbits inside of a burrow
     * if it can reproduce it does so and returns
     * if it has more than 60 energy it expands the burrow with a new entry
     * if its hunger is under 100 it exits burrow to go hunt
     * @param world
     */
    protected void inNestBehavior(World world) {
        if(reproduceBehavior(world)) return;
        if(getEnergy() > 60) {
            expandBurrow(world);
            return;
        }
        if(getHunger() < 100) exitNest(world);
    }

    /**
     * Gets the list of locations that the rabbit can exit the hole to.
     * The list consists of all emptylocation around the hole and the location of the hole itself if it empty
     * returns a random location from the list
     * @param world
     * @return
     */
    protected Location getExitLocation(World world) {
        Set<RabbitHole> entries = burrow.getEntries();
        Location freeLocation = null;

        for(Hole tempHole : entries) {
            if(!(freeLocation == null)) break;
            List<Location> emptyLocations = new ArrayList<>();
            if(world.isTileEmpty(tempHole.getLocation(world))) emptyLocations.add(tempHole.getLocation(world)); //adds hole itself to list
            emptyLocations.addAll(world.getEmptySurroundingTiles(tempHole.getLocation(world))); //adds empty sorrounding tiles to list
            if(emptyLocations.isEmpty()) break;
            int random = new Random().nextInt(emptyLocations.size());
            freeLocation = emptyLocations.get(random);
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
        strength = 100;
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

        return !(Helper.distance(world.getLocation(this), world.getLocation(nearestBurrowEntry)) * 5 > 25);
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
            Location location = Helper.findNonBlockingEmptyLocation(world);
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
