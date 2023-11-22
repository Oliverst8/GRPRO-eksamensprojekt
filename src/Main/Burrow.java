import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Burrow {
    private List<Hole> entries;
    private List<Rabbit> rabbitsInside;

    /**
     * Throws IllegalArgumentException if entry is null
     * Initialises the list of entries
     * Creates a hole with the entry, and adds it to the list
     * @param entry The first entry location of the burrow
     */
    public Burrow(World world, Location entry){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Initialises the list of entries
     * Creates a random location
     * Makes a hole with the location, and adds it to the list
     */
    public Burrow(World world){
        entries = new ArrayList<>();
        rabbitsInside = new ArrayList<>();
        Random random = new Random();
        Location entryLocation = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));
        addEntry(entryLocation, world);
    }

    /**
     * @return a list of Locations of the entries the burrow has
     */
    public List<Hole> getEntries(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalArgumentException if argument is null
     * Otherwise makes a hole and adds it to the list of entries
     * @param entry
     */
    public void addEntry(Location entry, World world){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
