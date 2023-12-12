package main;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

public interface Spawnable extends DynamicDisplayInformationProvider {
    /**
     * @return the class name of the object as a string
     */
    abstract String getType();

    /**
     * @return the color of the object, used when in top down view.
     */
    abstract Color getColor();

    /**
     * Builds a string of path to the png file of the animal
     * It determines weather or not the animal is adult, infected or sleeping
     * @return the completed path to the png file of the animal
     */
    abstract String getPNGPath();

    @Override
    default DisplayInformation getInformation() {
        return new DisplayInformation(getColor(), getPNGPath());
    }
}
