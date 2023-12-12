package main;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Den extends Nest {
    WolfHole hole;

    public Den(World world, Location entry) {
        hole = (WolfHole) ObjectFactory.generateOnMap(world,entry,"WolfHole");
    }

    public Location getLocation(World world) {
        return hole.getLocation(world);
    }
}
