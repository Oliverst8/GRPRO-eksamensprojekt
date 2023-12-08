package Main;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;

import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Rabbit extends NestAnimal {
    private Burrow burrow;


    /**
     * Initilises the food to the bunny can eat to plant and fruits
     * Initialises inBurrow to false
     */
    public Rabbit() {
        super(0);
        burrow = null;
        initialize();
    }

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

    private void initialize(){
        adultAge = 3;
        maxEnergy = 100;
        energy = maxEnergy;
        strength = 100;
        maxHealth = 100;
        health = maxHealth;
    }

    /**
     * No test written
     * If the bunny is sleeping make it run the sleep method
     * If it does not have a burrow:
     * - It checks what takes less energy, a make a burrow, or go to an exiting one (If they are equal it goes to the closest one)
     * If it does have one it moves towards its burrow if it isnt in it, otherwise it does nothing
     */
    @Override
    public void nightBehavior(World world) {
        if(isInfected()){
            fungi.infectedBehavior(world, this);
            return;
        }
        if(isInNest()) sleeping = true;
        if(sleeping){
            sleep();
            return;
        }
        goToNest(world);
    }



    protected void noNestBehavior(World world){
        if (shouldRabbitDig(world) && getEnergy() > 25) {
            dig(world);
        } else {
            setBurrow(((RabbitHole) findNearestPrey(world, 5, RabbitHole.class)).getBurrow());
            goToNest(world);
        }
    }

    private boolean shouldRabbitDig(World world) {
        RabbitHole nearestBurrowEntry = (RabbitHole) findNearestPrey(world, 5, RabbitHole.class);
        if(nearestBurrowEntry == null) return true;

        return !(distance(world, world.getLocation(nearestBurrowEntry)) * 5 > 25);
    }

    protected void moveTowardsNest(World world) {
        Location nearestEntry = burrow.findNearestEntry(world, world.getCurrentLocation());
        if(distance(world, nearestEntry) != 0) moveTowards(world, nearestEntry);
        if(distance(world, nearestEntry) == 0) enterNest(world);
    }

    protected void produceOffSpring(World world) {
        ObjectFactory.generateOffMap(world, "Rabbit", 0, burrow, true);
    }

    @Override
    void setupCanEat() {
        canEat.add(Grass.class);
    }

    /**
     * - If its in a burrow, check if it can reproduce
     * -    It can reproduce if there are two rabbits in the burrow, and they both have enough energy
     * - If it cant reproduce it tries to dig more entries to the burrow (If it has enough energy)
     * - If it exits burrow
     * - If out of borrow and hunger isnt full moves towards grass
     * - Otherwise
     */
    @Override
    public void dayBehavior(World world) {
        super.dayBehavior(world);
    }

    @Override
    protected void hungryBehavior(World world){
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
        hunt(world);
    }

    protected void inNestBehavior(World world){
            if(reproduceBehavior(world)) return;
            if(getEnergy() > 60) {
                expandBurrow(world);
                return;
            }
            if(getHunger() < 100) exitNest(world);
    }



    /**
     * Throws Main.IllegalOperationException if dig is called when the bunny already has a burrow
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
        setBurrow((Burrow) ObjectFactory.generateOnMap(world,"Burrow", world, world.getCurrentLocation()));
        removeEnergy(25);
    }

    /**
     * Throws Main.IllegalOperationException if the bunny has no burrow
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



    public Nest getNest(){
        return burrow;
    }



    protected Location getExitLocation(World world){
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
     * Throws IllegalArgumentException if burrow is null
     * Throws Main.IllegalOperationException if the bunny has a burrow already
     * Initializes the burrow to the argument
     * @param burrow The burrow which the bunny should make its own
     */
    private void setBurrow(Burrow burrow) {
        this.burrow = burrow;
    }



    @Override
    public void die(World world) {
        if(isInNest()) exitNest(world);
        super.die(world);

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
}
