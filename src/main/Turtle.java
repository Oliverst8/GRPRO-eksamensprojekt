package main;

import error.CantReproduceException;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Turtle extends Animal implements Oviparous{

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
    public Location getEggLocation(World world){

        Set<Location> surrondingLocations = world.getEmptySurroundingTiles(world.getCurrentLocation());
        List<Location> eggLocation = new ArrayList<>(surrondingLocations);

        if(eggLocation.isEmpty()) return null;
        else return eggLocation.get(0);
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
                if(Helper.distance(world.getLocation(this),world.getLocation(turtle)) < 2){
                    try{
                        reproduce(world,this,turtle);
                        return;
                    } catch (CantReproduceException e){
                        e.printInformation();
                    }
                }
            }
        }

        hunt(world);

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

    @Override
    public boolean isEatable(){
        return !inShell;
    }

    public int getShellHealth(){
        return shellHealth;
    }

    public boolean isInShell(){
        return inShell;
    }

    private void initialize() {
        adultAge = 3;

        strength = 1;

        maxEnergy = 250;
        maxHealth = 50;

        inShell = false;
        maxShellHealth = 2000;
        timeInShell = 0;

        energy = maxEnergy;
        health = maxHealth;
        shellHealth = maxShellHealth;
    }

    private void enterShell() {
        inShell = true;
        timeInShell = 0;
    }

    private void exitShell() {
        inShell = false;
        timeInShell = 0;
    }

    private void inShellBehavior() {
        timeInShell++;
        if(timeInShell >= 3){
            exitShell();
        }
    }
}
