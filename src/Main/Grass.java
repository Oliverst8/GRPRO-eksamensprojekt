package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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
        if(getEnergy()<25){
            throw new IllegalOperationException("Grass doesnt have energy to spread");
        }
        Set<Location> surroundingTiles = world.getEmptySurroundingTiles();
        List<Location> locationsList = new ArrayList<>(surroundingTiles);
        if(locationsList.size() <= 0) return;
        int randomIndex = new Random().nextInt(locationsList.size());
        Location randomLocation = locationsList.get(randomIndex);
        ObjectFactory.generateOnMap(world,randomLocation,"Grass", randomLocation, this);
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
     * Chance to spread scales with energy (The more energy the higer chance of spreading)
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    void dayBehavior(World world) {
    int random = new Random().nextInt(100-getEnergy());
    photosynthesis();
    if(random<10){
        spread(world);
        }
    }

    /**
     *
     * Calls spread
     * Calls photosynthesis
     * Bliver kaldt 10 gange på en dag
     * @param world
     */
    @Override
    void nightBehavior(World world) {
    decay();
    }


    @Override
    public String getPNGPath(){
        return getType();
    }
}
