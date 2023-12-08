package Main;

public abstract class Plant extends Organism implements Spawnable {


    public Plant(int defaultStenght) {
        super(defaultStenght);
        adultAge = 0;
        maxEnergy = 100;
        energy = maxEnergy;
    }

    /**
     * Degrades the current health by 10
     */
    protected void decay() {
        removeEnergy(5);
    }

    /**
     * Check if its day, if its day ->
     * Add 10 energy
     * else do nothing
     */
    protected void photosynthesis() {
        if(!isDay()) throw new IllegalOperationException("Cant make photosynthesis in the night");

        addEnergy(20);
    }
}