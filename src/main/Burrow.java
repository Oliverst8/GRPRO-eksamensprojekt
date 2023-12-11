package main;

import java.util.Set;
import java.util.Random;
import java.util.HashSet;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Burrow extends Nest{
    private final Set<RabbitHole> entries;

    /**
     * @throws IllegalArgumentException if entry is null or world is null
     * Initialises the list of entries
     * Initialises the list of rabbitsinside
     * Creates a hole with the entry, and adds it to the list
     * @param entry The first entry location of the burrow
     */
    public Burrow(World world, Location entry) {
        super();

        entries = new HashSet<>();

        addEntry(world, entry);
    }

    /**
     * Initialises the list of entries
     * Initialises the list of rabbitsinside
     * Creates a random location
     * Makes a hole with the location, and adds it to the list
     */
    public Burrow(World world) {
        super();

        entries = new HashSet<>();

        Random random = new Random();
        Location entryLocation = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));
        
        addEntry(world, entryLocation);
    }

    /**
     * @return a list of Locations of the entries the burrow has
     */
    public Set<RabbitHole> getEntries() {
        return entries;
    }

    /**
     * Throws IllegalArgumentException if argument is null
     * Otherwise makes a hole and adds it to the list of entries
     * @param entry
     */
    public void addEntry(World world, Location entry) {
        RabbitHole hole = (RabbitHole) ObjectFactory.generateOnMap(world,entry,"RabbitHole", this);

        entries.add(hole);
    }

    /**
     *
     * @param rabbitLocation the location of the rabbit
     * @return the closest entry, returns null if there is no entry found
     * returns closestEntryLocation if there is a entrance
     */
    public Location findNearestEntry(World world, Location rabbitLocation) {
        return findNearestEntity(world, rabbitLocation, entries);
    }
}
