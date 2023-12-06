package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Set;

public class Cordyceps implements Fungi{

    @Override
    public void hostDied(World world, MycoHost host) {
        MycoHost newHost = findNewHost(world, world.getLocation(host));
        if(newHost != null) newHost.setInfected(this);
        world.delete(host);
    }

    @Override
    public MycoHost findNewHost(World world, Location location) {
        Set<Entity> surroundingEntities = Helper.getEntities(world, location, 3);
        surroundingEntities.remove((Entity) world.getTile(location));
        surroundingEntities = Helper.filterByClass(surroundingEntities, Animal.class);

        Set<MycoHost> potentialHosts = filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return null;

        return (MycoHost) Helper.findNearest(world, location, potentialHosts);
    }
}
