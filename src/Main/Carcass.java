package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class Carcass extends Organism{

    private final Animal animal; //The animal that the carcass represents

    /**
     * Sets food chain value to -2
     * Makes the carcass enter the world
     * Sets the adult age of the carcass to 3
     * Sets the starting age of the carcass to 0
     * @param world the world the carcass is in
     * @param animal the type of carcass this is
     * @param carcassLocation where the carcass is located
     */
    public Carcass(World world, Animal animal, Location carcassLocation) {
        super(-2);

        this.animal = animal;
        world.setTile(carcassLocation, this);

        adultAge = 3;
        age = 0;
    }

    /**
     * @return the class of the animal the carcass is
     */
    @Override
    public Class<? extends Organism> getEntityClass(){
        return animal.getEntityClass();
    }

    /**
     * @return the dead animals energy
     */
    @Override
    public int getEnergy(){
        return animal.getEnergy();
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
