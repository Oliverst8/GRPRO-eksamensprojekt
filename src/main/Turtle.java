package main;

import itumulator.world.World;

import java.awt.*;

public class Turtle extends Animal{

    private boolean inShell;
    private int shellHealth;
    private int maxShellHealth;

    /**
     * Initialises hunger to 50
     * Initialises food type to that the animal itself is to meat
     * Initialises the food that can be eaten
     */
    public Turtle() {
        super(1);
        initialize();
    }

    @Override
    protected void setupCanEat() {
        addCanEat(Grass.class);
        addCanEat(Berry.class);
    }

    @Override
    protected void produceOffSpring(World world) {

    }

    @Override
    protected void dayBehavior(World world) {

    }

    @Override
    protected void nightBehavior(World world) {

    }

    @Override
    public String getType() {
        return "turtle";
    }

    @Override
    public Color getColor() {
        return new Color(144, 238, 144);
    }

    private void initialize() {
        adultAge = 3;

        strength = 1;

        maxEnergy = 250;
        maxHealth = 50;

        inShell = false;
        maxShellHealth = 500;

        energy = maxEnergy;
        health = maxHealth;
        shellHealth = maxShellHealth;
    }
}
