package Main;

import itumulator.simulator.Actor;
import itumulator.world.World;

public abstract class Organism extends Entity implements Actor, Consumable {
    protected int age;

    private boolean day;

    private boolean skipTurn = false;

    protected int adultAge;

    private boolean dead; //Contains data of weather or not the animal is dead

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
        dead = false;
        health = 100;
        setFoodChainValue(defualtFoodChainValue);
    }

    protected void setFoodChainValue(int foodChainValue) {
        this.foodChainValue = foodChainValue;
    }

    /**
     * @return the food chain value of the animal, and -1 if its dead
     */
    public int getFoodChainValue() {
        if(dead) return -1;
        return foodChainValue;
    }

    /**
     * @return the class of the object
     */
    @Override
    public Class<? extends Organism> getEntityClass() {
        return this.getClass();
    }

    public boolean isDead() {
        return dead;
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

        dead = true;
        world.delete(this);
    }

    public void setDead() {
        dead = true;
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

    protected boolean isDay() {
        return day;
    }

    protected void setDay(boolean day) {
        this.day = day;
    }

    abstract void dayBehavior(World world);

    abstract void nightBehavior(World world);
    private void doesAge() {
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
    public boolean isEatable(){
        return true;
    }

    @Override
    public void act(World world) {
        setDay(world.isDay());
        doesAge();
        if(dead || skipTurn) {
            skipTurn = false;
            return;
        }

        if(isDay()) dayBehavior(world);
        else nightBehavior(world);

        /*if(getEnergy() <= 0 || getHealth() <= 0) {
            System.out.println(this + " is out of energy or health and dying");
            die(world);
        }*/
    }

    protected boolean checkIfDying(World world){
        if(health <= 0 || energy <= 0){
            die(world);
            return true;
        }
        return false;
    }


    public void skipTurn(){
        skipTurn = true;
    }

    public void setSkipTurn(boolean skipTurn){
        this.skipTurn = skipTurn;
    }



    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public void removeHealth(int health, World world){
    setHealth(Math.max(0, this.health - health));
    if(this.health <=0) die(world);
    }

    public void addHealth(int health){
    setHealth(Math.max(100, this.health + health));
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getAdultAge() {
        return adultAge;
    }

    public int getMaxHealth(){
        return maxHealth;
    }
}
