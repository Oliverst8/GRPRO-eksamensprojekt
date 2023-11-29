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
        throw new UnsupportedOperationException("Not supported yet.");
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
     * If energy is less then 100
     * - Removes 10 hunger and adds 10 energy
     */
    protected void sleep(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getHunger() {
        return hunger;
    }
}
