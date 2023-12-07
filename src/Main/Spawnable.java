package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public interface Spawnable extends DynamicDisplayInformationProvider {

    abstract String getType();

    abstract Color getColor();

    abstract String getPNGPath();

    /**
     * @return the display information of the object
     */
    @Override
    default DisplayInformation getInformation() {
        return new DisplayInformation(getColor(), getPNGPath());
    }
}
