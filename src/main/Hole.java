package main;

import java.awt.Color;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

public abstract class Hole extends Inanimate implements NonBlocking {
    /**
     * @return the type of the hole
     */
    public abstract String getType();

    /**
     * @return the image of the hole
     */
    public abstract Color getColor();

    /**
     * @return the location of the hole
     * @param world the world the hole is in
     * @return the location of the hole
     */
    public Location getLocation(World world) {
        return world.getLocation(this);
    }
}
