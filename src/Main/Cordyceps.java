package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Set;

public class Cordyceps implements Fungi{
    @Override
    public void findNewHost(World world, MycoHost oldHost) {

        Set<Entity> surroundingEntities = Helper.getEntities(world, world.getLocation(oldHost), 3);
        surroundingEntities.remove(oldHost);
        surroundingEntities = Helper.filterByClass(surroundingEntities, MycoHost.class);

        Set<MycoHost> potentialHosts = Helper.filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return;

        MycoHost newHost = (MycoHost) Helper.findNearest(world, oldHost, potentialHosts);
        newHost.setInfected(this);
    }

    @Override
    public void hostDied(World world, MycoHost host) {
        findNewHost(world, host);
        world.delete(host);
    }
}
