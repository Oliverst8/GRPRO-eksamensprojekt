package Main;

import itumulator.executable.DisplayInformation;
import itumulator.world.World;

public class Grass extends Plant {


    /**
     * Sets the food type to plant
     */
    public Grass() {
        super("Main.Plant");
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

    /**
     * @return image of grass
     */
    @Override
    public DisplayInformation getInformation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
