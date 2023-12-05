package Main;

import itumulator.world.World;
import spawn.ObjectFactory;
import itumulator.world.Location;

public class Den extends Community{

    Hole den;

    public Den(World world, Location entry) {
        den = (Hole) ObjectFactory.generateOnMap(world,entry,"Hole");
    }

    public Location getLocation(World world) {
        return den.getLocation(world);
    }
}
