package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;

public abstract class ObjectsOnMap implements DynamicDisplayInformationProvider, Actor {

    protected abstract String getType();

    protected abstract Color getColor();

    protected abstract String getPNGPath();

    /**
     * @return the display information of the object
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(getColor(), getPNGPath());
    }
}
