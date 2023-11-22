package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.*;

public class Hole extends InAnimate implements NonBlocking {

    Location location;

    /**
     * Throws NullPointerException if the argument is null
     * Sets the location of the hole
     * @param location the location of the hole
     */
    public Hole(Location location){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Location getLocation() {
        return location;
    }

    @Override
    protected String getType() {
        return "hole";
    }

    @Override
    protected Color getColor() {
        return new Color(150, 75, 0);
    }

}
