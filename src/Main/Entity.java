package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public abstract class Entity implements DynamicDisplayInformationProvider {

    protected abstract String getType();

    protected abstract Color getColor();

    protected abstract String getPNGPath();

    /**
     * @return the class of the entity
     */
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }

    /**
     * @return the display information of the object
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(getColor(), getPNGPath());
    }
}
