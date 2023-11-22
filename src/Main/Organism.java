package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

import java.awt.*;

public abstract class Organism extends ObjectsOnMap implements Actor {
    protected int age;
    private String foodType; //The type of food the organism is
    private int energy; //0 is empty, and 100 is full

    private boolean day;

<<<<<<< HEAD
    protected int adultAge;

    public int getEnergyLossPerDay() {
        return energyLossPerDay;
    }

    protected int energyLossPerDay;

=======
>>>>>>> 8c3d0559f3cea860600d103151ec247dabc662ad
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
        energyLossPerDay = 5;
    }

    /**
     * Adds 1 to age
     */
    public void grow() {
        age++;
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
        return energy - (energyLossPerDay*(Math.max(0,getAge()-getAdultAge())));
    }

<<<<<<< HEAD
    /**
     * Removes energy
     * @param amount
     */
    public void RemoveEnergy(int amount){
        setEnergy(Math.max(0,getEnergy()-amount));
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

=======
>>>>>>> 8c3d0559f3cea860600d103151ec247dabc662ad
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

    public int getAdultAge() {
        return adultAge;
    }
}
