import itumulator.world.World;

abstract class Animal extends Organism {
    private int hunger; //0 is empty, and 100 is full
    private int energy; //0 is empty, and 100 is full

    /**
     * Initialises hunger to 50
     * Initialises energy to 100
     */
    public Animal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     */
    public void eat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void act(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
