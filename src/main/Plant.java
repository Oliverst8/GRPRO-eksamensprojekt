package main;

import error.IllegalOperationException;

public abstract class Plant extends Organism implements Spawnable {
    public Plant(int defaultStenght) {
        super(defaultStenght);
        adultAge = 0;
        maxEnergy = 100;
        energy = maxEnergy;
    }

    /**
     * Degrades the energy of the plant.
     */
    protected void decay() {
        removeEnergy(5);
    }

    /**
     * Check if it is day. If it is, add energy to the plant.
     * Else, throw an exception.
     */
    protected void photosynthesis() {
        if(!isDay()) throw new IllegalOperationException("Cant make photosynthesis in the night");
        addEnergy(20);
    }
}