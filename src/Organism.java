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
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return current health
     */
    public int getHealth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getFoodType(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
