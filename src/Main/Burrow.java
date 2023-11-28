package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Burrow {
    private List<Hole> entries;
    private List<Rabbit> rabbitsInside;

    /**
     * @throws IllegalArgumentException if entry is null or world is null
     * Initialises the list of entries
     * Initialises the list of rabbitsinside
     * Creates a hole with the entry, and adds it to the list
     * @param entry The first entry location of the burrow
     */
    public Burrow(World world, Location entry){
        if(entry == null) throw new NullPointerException("Entry cant be null");
        if (world == null) throw new NullPointerException("World cant be null");
        entries = new ArrayList<>();
        rabbitsInside = new LinkedList<>();
        addEntry(entry, world);
    }

    /**
     * @throws NullPointerException if world is null
     * Initialises the list of entries
     * Initialises the list of rabbitsinside
     * Creates a random location
     * Makes a hole with the location, and adds it to the list
     */
    public Burrow(World world){
        if (world == null) throw new NullPointerException("World cant be null");
        entries = new ArrayList<>();
        rabbitsInside = new LinkedList<>();
        Random random = new Random();
        Location entryLocation = new Location(random.nextInt(world.getSize()), random.nextInt(world.getSize()));
        addEntry(entryLocation, world);
    }

    /**
     * Add a rabbit to the burrow
     * @throws NullPointerException if argument is null
     * @param rabbit
     */
    public void addRabbit(Rabbit rabbit) {
        if(rabbit == null) throw new NullPointerException("Rabbit cant be null");
        rabbitsInside.add(rabbit);
    }

    /**
     * Remove a rabbit from the burrows
     * @throws NullPointerException if argument is null
     */
    public void removeRabbit(Rabbit rabbit) {
        if(rabbit == null) throw new NullPointerException("Rabbit cant be null");
        rabbitsInside.remove(rabbit);
    }

    /**
     * @return a list of Locations of the entries the burrow has
     */
    public List<Hole> getEntries(){
        return entries;
    }

    /**
     * Throws IllegalArgumentException if argument is null
     * Otherwise makes a hole and adds it to the list of entries
     * @param entry
     */
    public void addEntry(Location entry, World world){
        if (world == null) throw new NullPointerException("World cant be null");
        if (entry == null) throw new NullPointerException("Location cant be null");
        
        Hole hole;
        
        hole = (Hole) ObjectFactory.generateOnMap(world,entry,"Hole", entry, this);
        entries.add(hole);
    }

    /**
     * @return the list of rabbits inside the burrow
     */
    public List<Rabbit> getRabbitsInside(){
        return rabbitsInside;
    }

    public List<Rabbit> getAdultRabbitsInside(){
        List<Rabbit> adultRabbits = new ArrayList<>();
        for(Rabbit rabbit : rabbitsInside){
            if(rabbit.getAge() >= rabbit.getAdultAge()){
                adultRabbits.add(rabbit);
            }
        }
        return adultRabbits;
    }

    /**
     *
     * @param rabbitLocation the location of the rabbit
     * @return the closest entry, returns null if there is no entry found
     */
    public Location findNearestEntry(Location rabbitLocation) {
        Location closestEntryLocation = null;
        double minDist = Double.MAX_VALUE;
        for(Hole entry : entries){
            double distance = distance(rabbitLocation, entry.getLocation());
            if(minDist > distance){
                minDist = distance;
                closestEntryLocation = entry.getLocation();
            }
        }
        return closestEntryLocation;
    }

    /**
     * Finds the distance between two objects
     * @param location1 the first location
     * @param location2 the location of the second object
     * @return the distance between two object
     */
    private double distance(Location location1, Location location2) {
        return Math.sqrt(Math.pow(location1.getX() - location2.getX(), 2) + Math.pow(location1.getY() - location2.getY(), 2));
    }

}
