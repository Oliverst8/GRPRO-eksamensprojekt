package main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Oviparous {

    /**
     * Returns a empty location around the current location, where the egg can be
     * @param world the world the parent is in
     * @return Location where the egg can be
     */
    default Location getEggLocation(World world){
        Set<Location> surrondingLocations = world.getEmptySurroundingTiles(world.getCurrentLocation());
        List<Location> eggLocation = new ArrayList<>(surrondingLocations);

        if(eggLocation.isEmpty()) return null;
        else return eggLocation.getFirst();
    }

    /**
     * Lays an egg on the map
     * @param world the world the parent is in
     * If there is no empty location around the parent, the egg will not be laid
     */
    default void layEgg(World world){
        Location eggLocation = getEggLocation(world);
        if(eggLocation == null) return;
        ObjectFactory.generateOnMap(world, eggLocation, "Egg", this.getClass());
    }

}
