package main;

import java.awt.Color;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;

public class Carcass extends MycoHost implements DynamicDisplayInformationProvider {

    private Animal animal;
    private boolean spawned = false;
    private int startTick;

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
        health = 100;
        maxEnergy = 100;
    }

    @Override
    public Class<? extends Organism> getEntityClass() {
        if (animal != null) animal.getEntityClass();
        return getClass();
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public String getType() {
        return "carcass";
    }

    @Override
    public Color getColor() {
        return new Color(92, 64, 51);
    }

    @Override
    public String getPNGPath() {
        StringBuilder path = new StringBuilder();

        path.append(getType());

        if(animal != null && animal.maxHealth >= 200) path.append("-large");
        else path.append("-small");

        return path.toString();
    }

    @Override
    public void dayBehavior(World world) {
        carcassBehaviour(world);
    }

    @Override
    public void nightBehavior(World world) {
        carcassBehaviour(world);
    }

    /**
     * Is the behaviour for the carcuss both day and night
     * Removes 1 energy pr tick, so 20 per fulldaycycle
     * 100 ticks does not equal 100 energy taken since carcass also ages
     * maxiamal Energy that the organism can have gets less and less pr age
     * @param world
     */
    private void carcassBehaviour(World world) {
        if(isDying(world)) return;
        if(!spawned){
            startTick = world.getCurrentTime();
            spawned = true;
        }
        if(!isInfected()){
            if(age >= 2){
                setInfected(new Ghoul());
            }
        }
        if((world.getCurrentTime() == startTick)){
            return;
        }
        removeEnergy(1);
    }

    /**
     * Sets the animal that the carcass is from.
     * @param animal the animal that the carcass is from.
     */
    public void setAnimal(Animal animal) {
        this.animal = animal;
        this.energy = animal.getEnergy();
        this.maxEnergy = animal.getMaxEnergy();
        this.health = animal.maxHealth;
    }
}
