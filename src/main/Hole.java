package main;

import java.awt.Color;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

public class Hole extends Inanimate implements NonBlocking {

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
    public String getType() {
        return "hole-large";
    }

    /**
     * @return the color of the hole object
     */
    @Override
    public Color getColor() {
        return new Color(150, 75, 0);
    }
}
