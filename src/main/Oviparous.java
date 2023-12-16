package main;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public interface Oviparous {
    default Location getEggLocation(World world){
        Set<Location> surrondingLocations = world.getEmptySurroundingTiles(world.getCurrentLocation());
        List<Location> eggLocation = new ArrayList<>(surrondingLocations);

        if(eggLocation.isEmpty()) return null;
        else return eggLocation.get(0);
    }

    default void layEgg(World world){
        Location eggLocation = getEggLocation(world);
        if(eggLocation == null) return;
        ObjectFactory.generateOnMap(world, eggLocation, "Egg", this.getClass());
    }
}
