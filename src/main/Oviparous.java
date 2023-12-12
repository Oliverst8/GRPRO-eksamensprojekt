package main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Oviparous {
    default Location getEggLocation(World world){
        Set<Location> surrondingLocations = world.getEmptySurroundingTiles(world.getCurrentLocation());
        List<Location> eggLocation = new ArrayList<>(surrondingLocations);

        if(eggLocation.isEmpty()) return null;
        else return eggLocation.getFirst();
    }

    default void layEgg(World world){
        Location eggLocation = getEggLocation(world);
        if(eggLocation == null) return;
        ObjectFactory.generateOnMap(world, eggLocation, "Egg", this.getClass());
    }

}
