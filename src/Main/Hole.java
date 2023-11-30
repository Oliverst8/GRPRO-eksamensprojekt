package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.*;

public class Hole extends InAnimate implements NonBlocking {

    private Location location;

    private final Burrow burrow;

    private final String pngType;
    
    /**
     * Throws NullPointerException if the argument is null
     * Sets the location of the hole
     * @param location the location of the hole
     * @throws NullPointerException if the parameter location is null
     */
    public Hole(Location location, String pngType) {
        this.location = location;
        burrow = null;
        this.pngType = pngType;
    }

    public Hole(Location location, Burrow burrow, String pngType) {
        this.location = location;
        this.burrow = burrow;
        this.pngType = pngType;
    }

    /**
     * @return the location of the Hole object
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @return the type of the Hole object as hole
     */
    @Override
    protected String getType() {
        return pngType;
    }

    /**
     * @return the color of the hole object
     */
    @Override
    protected Color getColor() {
        return new Color(150, 75, 0);
    }

    public Burrow getBurrow() {
        return burrow;
    }
}
