package Main;

import java.awt.*;
import java.util.*;
import java.util.List;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import spawn.ObjectFactory;

public class Grass extends Plant implements NonBlocking {
    /**
     * Sets the food type to plant
     */
    public Grass() {
        super(-1);

        setEnergy(50);

        energyLossPerDay = 10;
    }

    /**
     * If the energy level is below 25 throw Main.IllegalOperationException
     * Create a new piece of grass next to this one
     * subtract 25 energy
     */
    private void spread(World world) {
        if(getEnergy() < 25) throw new IllegalOperationException("Grass doesnt have energy to spread");
        
        Set<Location> surroundingTiles = world.getEmptySurroundingTiles();
        List<Location> locationsList = new LinkedList<>(surroundingTiles);
        int randomIndex;
        Location randomLocation;

        do {
            if(locationsList.isEmpty()) return;
            randomIndex = new Random().nextInt(locationsList.size());
            randomLocation = locationsList.get(randomIndex);
            if(world.getTile(randomLocation) != null) locationsList.remove(randomIndex);
        } while(world.getTile(randomLocation) != null);

        ObjectFactory.generateOnMap(world,randomLocation,"Grass");

        removeEnergy(50);
    }

    @Override
    public String getType() {
        return "grass";
    }

    @Override
    public Color getColor() {
        return Color.green;
    }

    /**
     *
     * Calls photosynthesis
     * Will spread if the grass has more then or 50 energy
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void dayBehavior(World world) {
        photosynthesis();
        if(getEnergy() >= 75) spread(world);

    }

    /**
     *
     * Calls spread
     * Calls photosynthesis
     * Bliver kaldt 10 gange på en dag
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void nightBehavior(World world) {
        decay();
    }

    @Override
    public String getPNGPath() {
        return getType();
    }
}
