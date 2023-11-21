abstract class Organism {
    private int age;
    private int health;

    /**
     * Creates a new organism
     * Initialises age to 0
     * Initialises health to 100
     */
    public Organism() {
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
}
