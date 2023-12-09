package Main;

import itumulator.world.World;
import itumulator.simulator.Actor;

public abstract class Organism extends Entity implements Actor, Consumable {
    protected int age;

    private boolean day;

    private boolean skipTurn = false;

    protected int adultAge;

    private int foodChainValue;

    protected int energyLossPerDay;
    protected int maxHealth;
    protected int health;
    protected int energy;
    protected int maxEnergy;
    private boolean isNight;

    public int getEnergyLossPerDay() {
        return energyLossPerDay;
    }

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

    abstract void dayBehavior(World world);

    abstract void nightBehavior(World world);

    protected void setFoodChainValue(int foodChainValue) {
        this.foodChainValue = foodChainValue;
    }

    /**
     * @return the food chain value of the animal
     */
    public int getFoodChainValue() {
        return foodChainValue;
    }

    /**
     * @return the class of the object
     */
    @Override
    public Class<? extends Organism> getEntityClass() {
        return this.getClass();
    }

    /**
     * Adds 1 to age
     */
    public void grow() {
        age++;
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
     * @return current age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the current amount of energy
     */
    public int getEnergy() {
        return energy;
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

    public void setEnergy(int energy) {
        int newEnergy = (energy - (energyLossPerDay * (Math.max(0,getAge() - getAdultAge()))));
        this.energy = Math.max(0,Math.min(newEnergy, maxEnergy));
    }

    public boolean isDay() {
        return day;
    }

    public void setDay(boolean day) {
        this.day = day;
    }


    public void doesAge() {
        if(isNight && isDay()) {
            grow();
            isNight = false;
        } else if (!isDay()){
            isNight = true;
        }
    }

    /**
     * @return weather or not this organism can be eaten.
     */
    public boolean isEatable() {
        return true;
    }

    protected boolean isDying(World world) {
        if(health <= 0 || energy <= 0){
            die(world);
            return true;
        }
        return false;
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

    public void removeHealth(int health, World world) {
    setHealth(Math.max(0, this.health - health));
    if(this.health <=0) die(world);
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
}
