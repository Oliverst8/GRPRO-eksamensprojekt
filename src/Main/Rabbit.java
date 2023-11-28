package Main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Rabbit extends Animal {
    private Burrow burrow;
    private boolean inBurrow;

    /**
     * A rabbit that is not in a burrow
     * Initialises inBurrow to false
     */
    public Rabbit() {
        super(new String[]{"plant", "fruit"});

        burrow = null;
        inBurrow = false;
        adultAge = 3;
    }

    /**
     * A rabbit that is in a burrow
     * @param age the age of the rabbit
     * @param burrow the burrow that the rabbit is in
     */
    public Rabbit(int age, Burrow burrow) {
        super(new String[]{"plant", "fruit"});

        setBurrow(burrow);
        inBurrow = false;
        adultAge = 3;
        this.age = age;
    }

    /**
     * No test written
     * If the bunny is sleeping make it run the sleep method
     * If it does not have a burrow:
     * - It checks what takes less energy, a make a burrow, or go to an exiting one (If they are equal it goes to the closest one)
     * If it does have one it moves towards its burrow if it isnt in it, otherwise it does nothing
     */
    @Override
    protected void nightBehavior(World world) {
        if(inBurrow) sleeping = true;

        if(sleeping){
            sleep();
            return;
        }

        if(burrow != null){
            seekBurrow(world);
            return;
        }

        dig(world);
    }

    /**
     * Moves the bunny towards the closet hole entrance to its burrow
     * If the bunny stands on the burrow it will enter it
     * @param world
     */
    private void seekBurrow(World world) {
        if(burrow != null) {
            moveTowardsOwnBurrow(world);
        } else if(shouldRabbitDig(world)) {
            dig(world);
        } else {
            setBurrow( ((Hole) world.getNonBlocking(findNearest(world, 5, Hole.class))).getBurrow());
            seekBurrow(world);
        }
    }

    private boolean shouldRabbitDig(World world) {
        Location nearestBurrow = findNearest(world, 5, Burrow.class);
        if(nearestBurrow == null) return true;
        return !(distance(world, nearestBurrow) * 5 > 25);
    }

    private void moveTowardsOwnBurrow(World world) {
        Location nearestEntry = burrow.findNearestEntry(world.getCurrentLocation());
        if(distance(world, nearestEntry) != 0) moveTowards(nearestEntry, world);
        if(distance(world, nearestEntry) == 0) enterBurrow(world);
    }

    /**
     * - Set sleeping to false if its true, and call the grow method
     * - If its in a burrow, check if it can reproduce
     * -    It can reproduce if there are two rabbits in the burrow, and they both have enough energy
     * - If it cant reproduce it tries to dig more entries to the burrow (If it has enough energy)
     * - If it exits burrow
     * - If out of borrow and hunger isnt full moves towards grass
     * - Otherwise
     */
    @Override
    protected void dayBehavior(World world) {
        if(sleeping) {
            sleeping = false;
            grow();
        }

        if(inBurrow) {
            if(getEnergy() > 80 && burrow.getAdultRabbitsInside().size() >= 2){
                for(Rabbit otherRabbit : burrow.getAdultRabbitsInside()){
                    if(otherRabbit != this && otherRabbit.getEnergy() > 80){
                        reproduce(world, this, otherRabbit);
                        return;
                    }
                }
            }
            if(getEnergy() > 60){
                expandBurrow(world);
                return;
            }
            if(getHunger() < 100) exitBurrow(world);
        } else {
            if(getHunger() < 100) {
                Location nearestGrass = findNearest(world, 4, Grass.class);
                if (nearestGrass != null) {
                    Object grassTileObject = world.getTile(nearestGrass);
                    if(!(grassTileObject instanceof Grass || grassTileObject == this)) return;
                    if (distance(world, nearestGrass) == 0) {
                        eat((Grass) world.getNonBlocking(nearestGrass), world);
                    } else {
                        moveTowards(nearestGrass, world);
                    }
                }
            } else{
                seekBurrow(world);
            }
        }
    }

    /**
     * Throws Main.IllegalOperationException if dig is called when the bunny already has a burrow
     * If The bunny has at least 25 energy:
     * - calls makeBurrow()
     */
    private void dig(World world) {
        if(getEnergy()-25 > 0 ) {
            makeBurrow(world);
            enterBurrow(world);
        }
    }

    /**
     * - Makes a burrow at the current location
     *      *      * - Initialises burrow variable to newly created burrow
     *      *      * - Subtracts 25 energy
     */
    private void makeBurrow(World world) {
        setBurrow((Burrow) ObjectFactory.generate(world,"Burrow", world, world.getCurrentLocation()));
        removeEnergy(25);
    }

    /**
     * Throws Main.IllegalOperationException if the bunny has no burrow
     * If the bunny does not have 50 energy return;
     * - makes entrance to burrow
     * - removes 50 energy
     */
    private void expandBurrow(World world) {
        if(burrow == null) throw new IllegalArgumentException("Bunny cant expand a nonexistent burrow. Burrow is null");
        if(getEnergy()-50 > 0) {
            Location location = Helper.findNonBlockingEmptyLocation(world);
            burrow.addEntry(location, world);
            removeEnergy(50);
        }
    }

    /**
     * Throws Main.IllegalOperationException if the bunny is in a burrow or if the bunny has no burrow
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to true
     * removes the bunny from the world
     * When entering a burrow the rabbit goes to sleep
     */
    public void enterBurrow(World world) {
        if(inBurrow) throw new IllegalOperationException("Cant enter a burrow, if its already in one");
        if(world == null) throw new IllegalArgumentException("World is null");
        if(burrow == null) throw new NullPointerException("Burrow cant be null");

        inBurrow = true;
        burrow.addRabbit(this);
        world.remove(this);
        sleeping = true;
    }

    /**
     * Throws Main.IllegalOperationException if the bunny is not in a burrow or if the bunny has no burrow
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to false
     * Adds the bunny to the world in the location of a random burrow entry
     */
    private void exitBurrow(World world) {
        if(!inBurrow) throw new IllegalOperationException("Cant exit a burrow, if its not in one");
        if(world == null) throw new IllegalArgumentException("World is null");

        inBurrow = false;
        burrow.removeRabbit(this);
        List<Hole> entries = burrow.getEntries();
        world.setTile(entries.get(new Random().nextInt(entries.size())).getLocation(),this);
    }

    /**
     * Throws IllegalArgumentException if burrow is null
     * Throws Main.IllegalOperationException if the bunny has a burrow already
     * Initializes the burrow to the argument
     * @param burrow The burrow which the bunny should make its own
     */
    private void setBurrow(Burrow burrow) {
        if(burrow == null) throw new IllegalArgumentException("Burrow cant be null");
        if(this.burrow != null) throw new IllegalOperationException("Bunny already has a burrow");
        this.burrow = burrow;
    }

    /**
     * @return true if bunny is in burrow and false if not
     */
    public boolean isInBurrow() {
        return inBurrow;
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
