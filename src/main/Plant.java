package main;

import error.IllegalOperationException;

public abstract class Plant extends Organism {

    /**
     * creates new plant
     * calls organism constructor with the following parameter
     * @param defaultStrength
     * initialises adulgtage, maxenergy and sets energy as maxenergy
     */
    public Plant(int defaultStrength) {
        super(defaultStrength);
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