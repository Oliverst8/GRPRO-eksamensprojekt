package Main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Burrow extends Community{
    private List<Hole> entries;

    /**
     * @throws IllegalArgumentException if entry is null or world is null
     * Initialises the list of entries
     * Initialises the list of rabbitsinside
     * Creates a hole with the entry, and adds it to the list
     * @param entry The first entry location of the burrow
     */
    public Burrow(World world, Location entry) {
        super();
        if(entry == null) throw new NullPointerException("Entry cant be null");
        if(world == null) throw new NullPointerException("World cant be null");

        entries = new ArrayList<>();
        addEntry(entry, world);
    }

    /**
     * @throws NullPointerException if world is null
     * Initialises the list of entries
     * Initialises the list of rabbitsinside
     * Creates a random location
     * Makes a hole with the location, and adds it to the list
     */
    public Burrow(World world) {
        super();
        if (world == null) throw new NullPointerException("World cant be null");

        entries = new ArrayList<>();

        Random random = new Random();
        Location entryLocation = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));
        addEntry(entryLocation, world);
    }





    /**
     * @return a list of Locations of the entries the burrow has
     */
    public List<Hole> getEntries() {
        return entries;
    }

    /**
     * Throws IllegalArgumentException if argument is null
     * Otherwise makes a hole and adds it to the list of entries
     * @param entry
     */
    public void addEntry(Location entry, World world) {
        Hole hole = (Hole) ObjectFactory.generateOnMap(world,entry,"Hole", entry, this, "hole-small");

        entries.add(hole);
    }





    /**
     *
     * @param rabbitLocation the location of the rabbit
     * @return the closest entry, returns null if there is no entry found
     * returns closestEntryLocation if there is a entrance
     */
    public Location findNearestEntry(Location rabbitLocation) {
        Location closestEntryLocation = null;
        double minDist = Double.MAX_VALUE;

        for(Hole entry : entries) {
            double distance = Helper.distance(rabbitLocation, entry.getLocation());
            if(minDist > distance) {
                minDist = distance;
                closestEntryLocation = entry.getLocation();
            }
        }

        return closestEntryLocation;
    }


}
