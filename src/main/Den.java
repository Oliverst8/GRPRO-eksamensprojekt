package main;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Den extends Nest {
    WolfHole den;

    public Den(World world, Location entry) {
        den = (WolfHole) ObjectFactory.generateOnMap(world,entry,"WolfHole");
    }

    public Location getLocation(World world) {
        return den.getLocation(world);
    }
}
