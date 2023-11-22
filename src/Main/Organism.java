package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

public abstract class Organism implements Actor, DynamicDisplayInformationProvider {
    private int age;
    private int health; //0 is empty, and 100 is full
    private String foodType; //The type of food the organism is
    private int energy; //0 is empty, and 100 is full

    /**
     * Creates a new organism
     * Initialises age to 0
     * Initialises health to 100
     * Initialises the food type
     * Initialises energy to 100
     */
    public Organism(String foodType) {
        age = 0;
        health = 100;
        this.foodType = foodType;
        energy = 100;
    }

    /**
     * Adds 1 to age
     */
    public void grow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Kills this organism
     * @param world current world
     */
    public void die(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return current age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return current health
     */
    public int getHealth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return the food type of the organism
     */
    public String getFoodType(){
        return foodType;
    }

    /**
     * @return the current amount of energy
     */
    public int getEnergy(){
        return energy;
    }

    /**
     * @return the display information of the object
     */
    @Override
    public DisplayInformation getInformation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
