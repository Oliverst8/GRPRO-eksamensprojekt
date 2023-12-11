package main;

import itumulator.world.World;
import itumulator.simulator.Actor;

public abstract class Organism extends Entity implements Actor, Consumable {
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

    abstract void dayBehavior(World world);

    abstract void nightBehavior(World world);

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
     * @return the class of the object
     */
    @Override
    public Class<? extends Organism> getEntityClass() {
        return this.getClass();
    }


    public int getEnergyLossPerDay() {
        return energyLossPerDay;
    }

    /**
     * @throws NullPointerException if world is null
     * Kills this organism
     * @param world current world
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

    public void setEnergy(int energy) {
        int newEnergy = (energy - (energyLossPerDay * (Math.max(0,getAge() - getAdultAge()))));
        this.energy = Math.max(0,Math.min(newEnergy, maxEnergy));
    }

    public void doesAge() {
        if(isNight && isDay()) {
            grow();
            isNight = false;
        } else if (!isDay()){
            isNight = true;
        }
    }

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

    public void addEnergy(int amount) {
        setEnergy(Math.min(100, getEnergy() + amount));
    }

    public boolean isDay() {
        return day;
    }

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

    public void skipTurn() {
        skipTurn = true;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    public boolean isTurnSkipped() {
        return skipTurn;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void addHealth(int health) {
    setHealth(Math.max(100, this.health + health));
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getAdultAge() {
        return adultAge;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAdult() {
        return age >= adultAge;
    }

    protected boolean isDying(World world) {
        if(health <= 0 || energy <= 0){
            die(world);
            return true;
        }
        return false;
    }

    protected void setFoodChainValue(int foodChainValue) {
        this.foodChainValue = foodChainValue;
    }
}
