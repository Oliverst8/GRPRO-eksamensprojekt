package main;


import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.awt.*;

public class Egg extends Organism implements Spawnable{

    private final Class<? extends Oviparous> parentSpecies;
    private int hatchPercentage;

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

    private void behavior(World world){
        incubate(world);
    }

    @Override
    protected void nightBehavior(World world) {
        behavior(world);
    }

    private void initialize(){
        hatchPercentage = 0;

        energyLossPerDay = 0;

        maxEnergy = 100;
        maxHealth = 100;

        health = maxHealth;
        energy = maxEnergy;
    }

    private void incubate(World world){
        increaseHatchPercentage();
        if(hatchPercentage >= 100){
            hatch(world);
        }
    }

    private void hatch(World world){
        Location eggLocation = world.getLocation(this);
        die(world);

        String className = parentSpecies.toString().split(" ")[1];
        String classNameWithoutPackage = className.split("\\.")[1];

        ObjectFactory.generateOnMap(world, eggLocation, classNameWithoutPackage);
    }

    private void increaseHatchPercentage(){
        hatchPercentage = Math.min(100, hatchPercentage + 5);
    }
}
