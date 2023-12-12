package main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

public interface Oviparous {

    abstract Location getEggLocation(World world);

    default void layEgg(World world){
        Location eggLocation = getEggLocation(world);
        if(eggLocation == null) return;
        ObjectFactory.generateOnMap(world, eggLocation, "Egg", this.getClass());
    }

}
