package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.lang.reflect.InvocationTargetException;
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
        Hole hole = null;
        try {
            hole = (Hole) ObjectFactory.generate(world,entry,"hole");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException  e) {
            throw new RuntimeException(e.getMessage());
        }
        entries.add(hole);
    }

    /**
     * @return the list of rabbits inside the burrow
     */
    public List<Rabbit> getRabbitsInside(){
        return rabbitsInside;
    }

}
