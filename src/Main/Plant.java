package Main;

public abstract class Plant extends Organism {


    public Plant(int defaultStenght) {
        super(defaultStenght);
    }

    /**
     * Degrades the current health by 10
     */
    protected void decay() {
        removeEnergy(10);
    }

    /**
     * Check if its day, if its day ->
     * Add 10 energy
     * else do nothing
     */
    protected void photosynthesis() {
        if(!isDay()) {
            throw new IllegalOperationException("Cant make photosynthesis in the night");
        }

        addEnergy(10);
    }
}