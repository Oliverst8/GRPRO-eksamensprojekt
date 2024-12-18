package main;

import java.awt.Color;

import error.CantReproduceException;

import itumulator.world.World;

public class Turtle extends Animal implements Oviparous {
    private boolean inShell;
    private int shellHealth;
    private int maxShellHealth;
    private int timeInShell;

    /**
     * Initialises hunger to 50
     * Initialises food type to that the animal itself is to meat
     * Initialises the food that can be eaten
     */
    public Turtle() {
        super(1);
        initialize();
    }

    public Turtle(int age){
        super(1);
        initialize();
        this.age = age;
    }

    @Override
    public String getType() {
        return "turtle";
    }

    @Override
    public String getPNGPath(){
        StringBuilder path = new StringBuilder();

        path.append(getType());

        if(age >= adultAge) path.append("-large");
        else path.append("-small");

        if(inShell) path.append("-shell");
        else if(isInfected()) path.append("-fungi");

        return path.toString();
    }

    @Override
    public Color getColor() {
        return new Color(144, 238, 144);
    }

    @Override
    public boolean isEatable(){
        return !inShell;
    }

    @Override
    protected void setupCanEat() {
        addCanEat(Grass.class);
        addCanEat(Berry.class);
    }

    @Override
    protected void produceOffSpring(World world) {
        layEgg(world);
    }

    @Override
    protected void dayBehavior(World world) {

        if(inShell){
            inShellBehavior();
            return;
        }

        Bear nearestBear = (Bear) findNearestPrey(world, 3, Bear.class);
        if(nearestBear != null) {
            moveAwayFrom(world, world.getLocation(nearestBear));
            return;
        }

        if(getEnergy() > 70 && this.getAge() >= this.getAdultAge()){
            Turtle turtle = (Turtle) findNearestPrey(world, 4, Turtle.class);
            if(turtle != null && turtle.getEnergy() > 70 && turtle.getAge() >= turtle.getAdultAge()) {
                moveTowards(world, world.getLocation(turtle));
                if(Utility.distance(world.getLocation(this),world.getLocation(turtle)) < 2){
                    try{
                        reproduce(world,this,turtle);
                        return;
                    } catch (CantReproduceException e){
                        e.printInformation();
                    }
                }
            }
        }

        if(hunt(world)) return;

        wander(world);

    }

    @Override
    protected void nightBehavior(World world) {
        inShell = true;
        timeInShell = 3;
    }

    @Override
    public void removeHealth(int health, World world){
        if(shellHealth > 0){
            shellHealth = Math.max(0, shellHealth-health);
            enterShell();
        } else{
            super.removeHealth(health, world);
        }
    }

    /**
     * @return the current haelth of the shell.
     */
    public int getShellHealth(){
        return shellHealth;
    }

    /**
     * @return true if the turtle is in its shell, otherwise false.
     */
    public boolean isInShell(){
        return inShell;
    }

    /**
     * Initialises the turtle to its default values.
     */
    private void initialize() {
        adultAge = 3;

        damage = 1;

        maxEnergy = 250;
        maxHealth = 50;

        inShell = false;
        maxShellHealth = 450;
        timeInShell = 0;

        energy = maxEnergy;
        health = maxHealth;
        shellHealth = maxShellHealth;
    }

    /**
     * Enter the shell and reset the time in shell.
     */
    private void enterShell() {
        inShell = true;
        timeInShell = 0;
    }

    /**
     * Exit the shell and reset the time in shell.
     */
    private void exitShell() {
        inShell = false;
        timeInShell = 0;
    }

    /**
     * Determines the behavior of the turtle when it is in its shell.
     */
    private void inShellBehavior() {
        timeInShell++;
        if(timeInShell >= 3){
            exitShell();
        }
    }
}
