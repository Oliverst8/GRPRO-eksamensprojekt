package Main;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.*;

public class Hole extends InAnimate implements NonBlocking {

    private final Burrow burrow;

    private final String pngType;

    /**
     * Initialises the hole
     * @param pngType the image of the hole
     */
    public Hole(String pngType) {
        burrow = null;
        this.pngType = pngType;
    }

    /**
     * Initialises the hole belonging to a burrow
     * @param burrow the burrow the hole belongs to
     * @param pngType the image of the hole
     */
    public Hole(Burrow burrow, String pngType) {
        this.burrow = burrow;
        this.pngType = pngType;
    }

    /**
     * @return the location of the hole
     * @param world the world the hole is in
     * @return the location of the hole
     */
    public Location getLocation(World world) {
        return world.getLocation(this);
    }

    /**
     * @return the image of the hole
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

    /**
     * @return the burrow the hole belongs to
     */
    public Burrow getBurrow() {
        return burrow;
    }
}
