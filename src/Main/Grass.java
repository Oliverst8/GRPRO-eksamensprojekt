package Main;

import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;

public class Grass extends Plant {


    /**
     * Sets the food type to plant
     */
    public Grass() {
        super("plant");
    }

    /**
     * If day:
     * - Calls photosynthesis
     * Chance to spread scales with energy (The more energy the higer chance of spreading)
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {

    }

    /**
     * If the energy level is below 25 throw Main.IllegalOperationException
     * Create a new piece of grass next to this one
     * subtract 25 energy
     */
    private void spread(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getType() {
        return "grass";
    }

    @Override
    protected Color getColor() {
        return Color.green;
    }

    @Override
    Organism reproduce() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
