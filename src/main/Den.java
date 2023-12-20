package main;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Den extends Community implements Nest {
    WolfHole hole;

    /**
     * Creates a new den at the given location.
     * @param world the world the den is in.
     * @param entry the location of the den.
     */
    public Den(World world, Location entry) {
        hole = (WolfHole) ObjectFactory.generateOnMap(world,entry,"WolfHole");
    }

    /**
     * Finds and returns the location of the hole belonging to the den.
     * @param world the world the den is in.
     * @return the location of the hole.
     */
    public Location getLocation(World world) {
        return hole.getLocation(world);
    }
}
