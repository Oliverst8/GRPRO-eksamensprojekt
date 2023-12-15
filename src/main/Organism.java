package main;

import itumulator.world.World;
import itumulator.simulator.Actor;

public abstract class Organism extends Entity implements Actor {
    protected int age;
    protected int adultAge;
    protected int energyLossPerDay;
    protected int maxHealth;
    protected int health;
    protected int energy;
    protected int maxEnergy;
    private boolean day;
    private boolean skipTurn = false;
    private int foodChainValue;
    private boolean isNight;

    /**
     * Creates a new organism
     * Initialises age to 0
     * Initialises the food type
     * Initialises energy to 100
     */
    public Organism(int defualtFoodChainValue) {
        isNight = false;
        age = 0;
        energyLossPerDay = 5;
        health = 100;
        setFoodChainValue(defualtFoodChainValue);
    }

    /**
     * Determines the behavior of the animal during the day
     * @param world the world which the animal is in
     */
    abstract void dayBehavior(World world);

    /**
     * Determines the behavior of the animal during the night
     * @param world the world which the animal is in
     */
    abstract void nightBehavior(World world);

    /**
     * The act of the organism, gets called every tick
     * sets day if it is day, calls doesage and if the organism has skipturn it sets it to false and returns
     * if it is day it calls daybehaviour and if it is night it calls nightbehavior
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        setDay(world.isDay());
        doesAge();
        if(isTurnSkipped()) {
            setSkipTurn(false);
            return;
        }

        if(isDay()) dayBehavior(world);
        else nightBehavior(world);

    }

    /**
     * @return the class of the organism that this is called from
     */
    @Override
    public Class<? extends Organism> getEntityClass() {
        return this.getClass();
    }

    /**
     * @return the amount of energy lost per day.
     */
    public int getEnergyLossPerDay() {
        return energyLossPerDay;
    }

    /**
     * Kills this organism.
     * @param world current world the organism is in.
     */
    public void die(World world) {
        world.delete(this);
    }

    /**
     * @return the current amount of energy
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the energy of the organism to the value given by the following:
     * @param energy
     */
    public void setEnergy(int energy) {
        int newEnergy = (energy - (energyLossPerDay * (Math.max(0,getAge() - getAdultAge()))));
        this.energy = Math.max(0,Math.min(newEnergy, maxEnergy));
    }

    /**
     * Does age function
     * makes the organism age with 1 if it is both night and day at tick 0
     * if it is not that, then its night (true)
     */
    public void doesAge() {
        if(isNight && isDay()) {
            grow();
            isNight = false;
        } else if (!isDay()){
            isNight = true;
        }
    }

    /**
     * removes the amount of health from organism as given from the following parameter:
     * @param health
     * @param world if the amount of health removed is more that its current health resulting in 0 health, then the organism dies from this world
     */
    public void removeHealth(int health, World world) {
        setHealth(Math.max(0, this.health - health));
        if(this.health <=0) die(world);
    }

    /**
     * @return current age
     */
    public int getAge() {
        return age;
    }

    /**
     * Removes energy
     * @param amount
     */
    public void removeEnergy(int amount) {
        setEnergy(Math.max(0, getEnergy() - amount));
    }

    /**
     * @param amount
     * adds the value of the parameter value to the organisms current energy
     */
    public void addEnergy(int amount) {
        setEnergy(Math.min(100, getEnergy() + amount));
    }

    /**
     * @return returns day
     */
    public boolean isDay() {
        return day;
    }

    /**
     * sets day if it isDay
     * @param day
     */
    public void setDay(boolean day) {
        this.day = day;
    }

    /**
     * @return weather or not this organism can be eaten.
     */
    public boolean isEatable() {
        return true;
    }

    /**
     * @return the food chain value of the animal
     */
    public int getFoodChainValue() {
        return foodChainValue;
    }

    /**
     * Adds 1 to age
     */
    public void grow() {
        age++;
    }

    /**
     * sets skipturn to true, resulting in the organism not acting for one tick
     */
    public void skipTurn() {
        skipTurn = true;
    }

    /**
     * sets global skipturn value to skipturns current value
     * @param skipTurn
     */
    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    /**
     * @return wether or not it gets skipped this turn
     */
    public boolean isTurnSkipped() {
        return skipTurn;
    }

    /**
     * @return current health of the organism
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health sets health of the organism to the value of the parameter
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @param health adds health of the organism to the value of the parameter
     */
    public void addHealth(int health) {
    setHealth(Math.max(100, this.health + health));
    }

    /**
     * @return maxenergy of the organism
     */
    public int getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * @return adultage of the organism
     */
    public int getAdultAge() {
        return adultAge;
    }

    /**
     * @return maxhealth of the organism
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return true if the organism has a age bigger or equal to its adultage
     */
    public boolean isAdult() {
        return age >= adultAge;
    }

    /**
     * returns true if the organism is currently dying
     * @param world the world that the organism dies in
     * @return false if the organism is not dying
     */
    protected boolean isDying(World world) {
        if(health <= 0 || energy <= 0){
            die(world);
            return true;
        }
        return false;
    }

    /**
     * sets the foodchainvalue of the organism to the value of the param
     * @param foodChainValue
     */
    protected void setFoodChainValue(int foodChainValue) {
        this.foodChainValue = foodChainValue;
    }
}
