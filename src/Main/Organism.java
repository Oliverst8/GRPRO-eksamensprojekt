package Main;

import itumulator.simulator.Actor;
import itumulator.world.World;

public abstract class Organism extends Entity implements Actor{
    protected int age;
    private String foodType; //The type of food the organism is
    private int energy; //0 is empty, and 100 is full

    private boolean day;

    protected int adultAge;

    public int getEnergyLossPerDay() {
        return energyLossPerDay;
    }

    protected int energyLossPerDay;

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
     * @throws NullPointerException if world is null
     * Kills this organism
     * @param world current world
     */
    public void die(World world) {
        if(world == null) throw new NullPointerException("World cant be null");
        world.delete(this);
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

    /**
     * Removes energy
     * @param amount
     */
    public void removeEnergy(int amount){
        setEnergy(Math.max(0,getEnergy()-amount));
    }
    public void addEnergy(int amount){
        setEnergy(Math.min(100,getEnergy()+amount));
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    protected boolean isDay() {
        return day;
    }

    protected void setDay(boolean day) {
        this.day = day;
    }

    abstract void dayBehavior(World world);

    abstract void nightBehavior(World world);

    @Override
    public void act(World world) {
        if(world == null) throw new NullPointerException("world cant be null");

        setDay(world.isDay());

        if (isDay()) dayBehavior(world);
        else nightBehavior(world);

        if(getEnergy() == 0) {
            System.out.println(this + " is out of energy and dying");
            die(world);
        }
    }

    @Override
    protected String getPNGPath(){
        StringBuilder path = new StringBuilder();

        path.append(getType());

        if(age >= adultAge) path.append("-large");
        else path.append("-small");

        return path.toString();
    }

    public int getAdultAge() {
        return adultAge;
    }
}
