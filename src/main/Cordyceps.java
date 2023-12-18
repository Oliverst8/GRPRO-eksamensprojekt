package main;

import java.util.Set;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Cordyceps implements Fungi {
    @Override
    public void infectedBehavior(World world, MycoHost host) {
        drain(world, host);
        if(host.getEnergy() <= 0 || host.getHealth() <= 0) return;

        Set<Entity> surrondingEntities = Utility.getEntities(world, world.getLocation(host), 3);
        surrondingEntities = Utility.filterByClass(surrondingEntities, host.getClass());

        surrondingEntities.retainAll(filterNonInfectedMycoHosts(surrondingEntities));

        Entity closetAnimalOfSameSpecies = Utility.findNearest(world, world.getLocation(host), surrondingEntities);
        if(closetAnimalOfSameSpecies == null) return;

        Animal animalHost = (Animal) host;
        animalHost.moveTowards(world, world.getLocation(closetAnimalOfSameSpecies));
    }

    @Override
    public void drain(World world, MycoHost host) {
        host.removeEnergy(5);
    }

    @Override
    public void hostDied(World world, MycoHost host) {
        Location hostLocation = world.getLocation(host);

        Animal newHost = (Animal) findNewHost(world, hostLocation);

        if(newHost != null) {
            newHost.setInfected(this);
        }
        else {
            if(!world.containsNonBlocking(hostLocation)) ObjectFactory.generateOnMap(world, hostLocation, "Grass");
        }

        world.delete(host);
    }

    @Override
    public MycoHost findNewHost(World world, Location location) {
        Set<Entity> surroundingEntities = Utility.getEntities(world, location, 3);
        surroundingEntities.remove((Entity) world.getTile(location));
        surroundingEntities = Utility.filterByClass(surroundingEntities, Animal.class);

        Set<MycoHost> potentialHosts = filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return null;

        return (MycoHost) Utility.findNearest(world, location, potentialHosts);
    }
}
