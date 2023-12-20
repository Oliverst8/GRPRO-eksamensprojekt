package main;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public interface Oviparous {

    /**
     * returns a location where the egg can be laid
     * @param world the world which the animal is in
     */
    default Location getEggLocation(World world){
        Set<Location> surroundingLocations = world.getEmptySurroundingTiles(world.getCurrentLocation());
        List<Location> eggLocation = new ArrayList<>(surroundingLocations);

        if(eggLocation.isEmpty()) return null;
        else return eggLocation.get(0);
    }

    /**
     * lays an egg at the location returned by getEggLocation
     * @param world the world which the animal is in
     */
    default void layEgg(World world) {
        Location eggLocation = getEggLocation(world);
        if(eggLocation == null) return;
        ObjectFactory.generateOnMap(world, eggLocation, "Egg", this.getClass());
    }
}
