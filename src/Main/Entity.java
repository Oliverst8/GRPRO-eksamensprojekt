package Main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public abstract class Entity {



    /**
     * @return the class of the entity
     */
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }



}
