import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.util.List;

public class Burrow implements Actor, NonBlocking {
    private List<Location> entries;

    /**
     * Throws IllegalArgumentException if entry is null
     * Initialises the list of entries
     * Adds the argument to the list of entries
     * @param entry The first entry location of the burrow
     */
    public Burrow(Location entry){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return a list of Locations of the entries the burrow has
     */
    public List<Location> getEntries(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalArgumentException if argument is null
     * Otherwise adds argument to list of entries
     * @param entry
     */
    public void addEntry(Location entry){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
