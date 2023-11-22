package Main;

import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal extends Organism {

    private int hunger; //0 is empty, and 100 is full

    private final String[] canEat;

    /**
     * Initialises hunger to 50
     * Initialises food type to meat
     * Initialises the food that can be eaten
     */
    public Animal(String[] canEat) {
        super("Meat");
        hunger = 50;
        this.canEat = canEat;
    }

    /**
     * Check weather or not the animal can eat the food
     * Adds energy if it can, and does nothing if not.
     * @param food the food to be eaten
     */
    protected void eat(Organism food) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throw IllegalArgumentException if world is null or radius is less then 2
     * Finds the nearest object of the type object to this animal
     * @return the location of the nearest grass
     */
    protected Location findNearest(World world, int radius, Object object){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Finds the distance between two objects
     * @param world the world the animal is in
     * @param location2 the location of the second object
     * @return the distance between two object
     */
    private double distance(World world, Location location2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Animal reproduce(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * If energy is less then 100
     * - Removes 10 hunger and adds 10 energy
     */
    protected void sleep(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getHunger() {
        return hunger;
    }

    public String[] getCanEat(){
        return canEat;
    }
}
