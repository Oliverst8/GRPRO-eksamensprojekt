package Main;

import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.*;

public class Hole extends InAnimate implements NonBlocking {

    private Location location;

    private final Burrow burrow;
    /**
     * Throws NullPointerException if the argument is null
     * Sets the location of the hole
     * @param location the location of the hole
     * @throws NullPointerException if the parameter location is null
     */
    public Hole(Location location){
        if(location == null) throw new NullPointerException("location of the hole cant be null");

        this.location = location;
        burrow = null;
    }

    public Hole(Location location, Burrow burrow){
        if(location == null) throw new NullPointerException("location of the hole cant be null");
        if(burrow == null) throw new NullPointerException("Burrow cant be null");

        this.location = location;
        this.burrow = burrow;
    }

    /**
     * @return the location of the Hole object
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @return the type of the Hole object as hole
     */
    @Override
    protected String getType() {
        return "hole";
    }

    /**
     * @return the color of the hole object
     */
    @Override
    protected Color getColor() {
        return new Color(150, 75, 0);
    }

    public Burrow getBurrow(){
        return burrow;
    }
}
