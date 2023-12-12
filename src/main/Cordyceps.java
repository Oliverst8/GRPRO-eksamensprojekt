package main;

import java.util.Set;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Cordyceps implements Fungi {
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
        Set<Entity> surroundingEntities = Helper.getEntities(world, location, 3);
        surroundingEntities.remove((Entity) world.getTile(location));
        surroundingEntities = Helper.filterByClass(surroundingEntities, Animal.class);

        Set<MycoHost> potentialHosts = filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return null;

        return (MycoHost) Helper.findNearest(world, location, potentialHosts);
    }

    @Override
    public void drain(World world, MycoHost host) {
        host.removeEnergy(5);
    }

    @Override
    public void infectedBehavior(World world, MycoHost host) {
        drain(world, host);
        if(host.getEnergy() <= 0 || host.getHealth() <= 0) return;

        Set<Entity> surrondingEntities = Helper.getEntities(world, world.getLocation(host), 3);
        surrondingEntities = Helper.filterByClass(surrondingEntities, host.getClass());

        surrondingEntities.retainAll(filterNonInfectedMycoHosts(surrondingEntities));

        Entity closetAnimalOfSameSpecies = Helper.findNearest(world, world.getLocation(host), surrondingEntities);
        if(closetAnimalOfSameSpecies == null) return;

        Animal animalHost = (Animal) host;
        animalHost.moveTowards(world, world.getLocation(closetAnimalOfSameSpecies));
    }
}
