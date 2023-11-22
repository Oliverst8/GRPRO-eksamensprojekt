package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

import java.awt.*;

public abstract class Organism extends ObjectsOnMap implements Actor {
    private int age;
    private String foodType; //The type of food the organism is
    private int energy; //0 is empty, and 100 is full

    private boolean day;




    /**
     * Creates a new organism
     * Initialises age to 0
     * Initialises the food type
     * Initialises energy to 100
     */
    public Organism(String foodType) {
        age = 0;
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






    protected boolean isDay() {
        return day;
    }

    protected void setDay(boolean day) {
        this.day = day;
    }

    @Override
    protected String getPNGPath(){
        return getType();
    }
}
