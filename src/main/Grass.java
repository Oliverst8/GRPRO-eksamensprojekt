package main;

import java.awt.Color;

import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import spawn.ObjectFactory;

import error.IllegalOperationException;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

public class Grass extends Plant implements NonBlocking {
    /**
     * Sets the food type to plant
     */
    public Grass() {
        super(-1);

        setEnergy(50);

        energyLossPerDay = 10;
    }

    @Override
    public String getType() {
        return "grass";
    }

    @Override
    public Color getColor() {
        return Color.green;
    }

    @Override
    public void dayBehavior(World world) {
        isDying(world);
        photosynthesis();
        if(getEnergy() >= 75) spread(world);

    }

    @Override
    public void nightBehavior(World world) {
        decay();
        isDying(world);
    }

    @Override
    public String getPNGPath() {
        return getType();
    }

    /**
     * If the energy level is below 25 throw Error.IllegalOperationException
     * Create a new piece of grass next to this one
     * subtract 25 energy
     */
    private void spread(World world) {
        if(getEnergy() < 25) throw new IllegalOperationException("Grass doesnt have energy to spread");
        
        Set<Location> surroundingTiles = world.getEmptySurroundingTiles();
        List<Location> locationsList = new ArrayList<>(surroundingTiles);
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
}
