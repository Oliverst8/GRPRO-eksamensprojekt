package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Grass extends Plant implements NonBlocking {
    /**
     * Sets the food type to plant
     */
    public Grass() {
        super("plant");
    }

    /**
     * If the energy level is below 25 throw Main.IllegalOperationException
     * Create a new piece of grass next to this one
     * subtract 25 energy
     */
    private void spread(World world) {
        if(getEnergy()<25) throw new IllegalOperationException("Grass doesnt have energy to spread");
        
        Set<Location> surroundingTiles = world.getEmptySurroundingTiles();
        List<Location> locationsList = new LinkedList<>(surroundingTiles);
        int randomIndex;
        Location randomLocation;

        do{
            if(locationsList.size() <= 0) return;
            randomIndex = new Random().nextInt(locationsList.size());
            randomLocation = locationsList.get(randomIndex);
            if(world.getTile(randomLocation) != null) locationsList.remove(randomIndex);
        } while(world.getTile(randomLocation) != null);

        ObjectFactory.generateOnMap(world,randomLocation,"Grass");

        removeEnergy(25);
    }

    @Override
    protected String getType() {
        return "grass";
    }

    @Override
    protected Color getColor() {
        return Color.green;
    }

    /**
     *
     * Calls photosynthesis
     * Will spread if the grass has more then or 50 energy
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    void dayBehavior(World world) {
        photosynthesis();
        if(getEnergy() >= 50) {
            spread(world);
        }
    }

    /**
     *
     * Calls spread
     * Calls photosynthesis
     * Bliver kaldt 10 gange på en dag
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    void nightBehavior(World world) {
        decay();
    }

    @Override
    public String getPNGPath() {
        return getType();
    }
}
