package main;

import java.util.Set;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public interface Fungi {
    /**
     * Drains the host of its energy.
     * @param world the world the host is in.
     * @param host the host to drain.
     */
    abstract void drain(World world, MycoHost host);
    
    /**
     * Handles the death of the host.
     * @param world the world the host is in.
     * @param host the host that died.
     */
    abstract void hostDied(World world, MycoHost host);

    /**
     * Handles the behavior of the fungi when it has infected a host.
     * @param world the world the host is in.
     * @param host the host that is infected.
     */
    abstract void infectedBehavior(World world, MycoHost host);

    /**
     * Finds a new host for the fungi.
     * @param world the world the fungi is in.
     * @param location the location of the fungi.
     * @return the new host.
     */
    abstract MycoHost findNewHost(World world, Location location);

    /**
     * Filters out the non-infected hosts from a set of entities.
     * @param entities the set of entities to filter.
     * @return the set of non-infected hosts.
     */
    default Set<MycoHost> filterNonInfectedMycoHosts(Set<Entity> entities) {
        Set<MycoHost> nonInfected = new HashSet<>();
        
        for(Entity entity : entities) {
            MycoHost host = (MycoHost) entity;
            if(!host.isInfected()) nonInfected.add(host);
        }

        return nonInfected;
    }
}
