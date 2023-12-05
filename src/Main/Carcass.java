package Main;

import itumulator.world.World;

import java.awt.*;

public class Carcass extends Organism {

    private Animal animal;

    /**
     * Sets food chain value to -2
     * Sets the adult age of the carcass to 3
     * Sets the starting age of the carcass to 0
     */
    public Carcass() {
        super(-2);

        adultAge = 3;
        age = 0;
        energy = 100;
        animal = null;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
        this.energy = animal.getEnergy();
    }

    /**
     * @return the class of the animal the carcass is
     */
    @Override
    public Class<? extends Organism> getEntityClass() {
        if (animal == null) animal.getEntityClass();
        return getClass();
    }

    /**
     * @return the dead animals energy
     */
    @Override
    public int getEnergy() {
        return energy;
    }

    /**
     * @return the type of organism this is
     */
    @Override
    protected String getType() {
        return "carcass";
    }

    /**
     * @return the default color of the carcuss (Brown)
     */
    @Override
    protected Color getColor() {
        return new Color(92, 64, 51);
    }


    /**
     * If it has been night and is now day the carcass gets older
     * @param world the world the carcass is in
     * If the carcass reaches its adult age it dies
     */
    @Override
    void dayBehavior(World world) {
        if(age == adultAge) die(world);
    }

    /**
     * Sets the night variable to true
     * @param world the world the carcass is in
     */
    @Override
    void nightBehavior(World world) {}
}
