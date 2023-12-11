package main;

import java.util.Set;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public interface Fungi {
    abstract void hostDied(World world, MycoHost host);

    abstract MycoHost findNewHost(World world, Location location);

    default Set<MycoHost> filterNonInfectedMycoHosts(Set<Entity> entities) {
        Set<MycoHost> nonInfected = new HashSet<>();
        
        for(Entity entity : entities) {
            MycoHost host = (MycoHost) entity;
            if(!host.isInfected()) nonInfected.add(host);
        }

        return nonInfected;
    }

    abstract void drain(World world, MycoHost host);

    abstract void infectedBehavior(World world, MycoHost host);
}
