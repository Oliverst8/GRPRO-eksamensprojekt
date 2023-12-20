package main;

import java.awt.Color;

import spawn.ObjectFactory;

import itumulator.world.Location;
import itumulator.world.World;

public class Egg extends Organism {
    private final Class<? extends Oviparous> parentSpecies;
    private int hatchPercentage;

    /**
     * Creates an egg with the given parent species.
     * @param parentSpecies the parent species of the egg.
     */
    public Egg(Class<? extends Oviparous> parentSpecies) {
        super(-2);
        this.parentSpecies = parentSpecies;
        initialize();
    }

    @Override
    public String getType() {
        return "Egg";
    }

    @Override
    public Color getColor() {
        return Color.white;
    }

    @Override
    public String getPNGPath() {
        return getType();
    }

    @Override
    protected void dayBehavior(World world) {
        behavior(world);
    }

    @Override
    protected void nightBehavior(World world) {
        behavior(world);
    }

    /**
     * Initializes the egg to default values.
     */
    private void initialize() {
        hatchPercentage = 0;

        energyLossPerDay = 0;

        maxEnergy = 100;
        maxHealth = 100;

        health = maxHealth;
        energy = maxEnergy;
    }

    /**
     * Determines the behavior of the egg
     * @param world the world which the egg is in.
     */
    private void behavior(World world) {
        incubate(world);
    }

    /**
     * Increases the hatch percentage of the egg.
     * If the hatch percentage is 100 or more, the egg hatches.
     * @param world the world which the egg is in.
     */
    private void incubate(World world) {
        increaseHatchPercentage();
        if(hatchPercentage >= 100){
            hatch(world);
        }
    }

    /**
     * Hatches the egg and spawns the animal.
     * @param world the world which the egg is in.
     */
    private void hatch(World world) {
        Location eggLocation = world.getLocation(this);
        die(world);

        String className = parentSpecies.toString().split(" ")[1];
        String classNameWithoutPackage = className.split("\\.")[1];

        ObjectFactory.generateOnMap(world, eggLocation, classNameWithoutPackage);
    }

    /**
     * Increases the hatch percentage of the egg.
     */
    private void increaseHatchPercentage(){
        hatchPercentage = Math.min(100, hatchPercentage + 5);
    }
}
