package Main;

import java.awt.*;

public abstract class Plant extends Organism {

    /**
     * Sets the food type of the plant
     * @param foodType
     */
    public Plant(String foodType) {
        super(foodType);
    }

    /**
     * Degrades the current health by 10
     */
    protected void decay() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Add 10 energy
     */
    protected void photosynthesis(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}