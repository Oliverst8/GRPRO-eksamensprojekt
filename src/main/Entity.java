package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public abstract class Entity implements DynamicDisplayInformationProvider {

    /**
     * @return the class name of the object as a string
     */
    public abstract String getType();

    /**
     * @return the color of the object, used when in top down view.
     */
    public abstract Color getColor();

    /**
     * Builds a string of path to the png file of the animal
     * It determines weather or not the animal is adult, infected or sleeping
     * @return the completed path to the png file of the animal
     */
    public abstract String getPNGPath();

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(getColor(), getPNGPath());
    }

    /**
     * @return the class of the entity
     */
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }
}
