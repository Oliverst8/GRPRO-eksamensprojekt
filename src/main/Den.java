package main;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Den extends Community implements Nest {
    WolfHole hole;

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
