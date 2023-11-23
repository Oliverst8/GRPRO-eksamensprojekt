package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class Rabbit extends Animal {

    private Burrow burrow;
    private boolean inBurrow;

    /**
     * Initilises the food to the bunny can eat to plant and fruits
     * Initialises inBurrow to false
     */
    public Rabbit(){
        super(new String[]{"plant", "fruit"});
        inBurrow = false;
        adultAge = 3;
    }

    public Rabbit(int age){
        super(new String[]{"plant", "fruit"});
        inBurrow = false;
        adultAge = 3;
        this.age = age;
    }

    /**
     * Throws IllegalArgumentException if world is null
     * Act check if its night or day
     * If its night it calls night behavior
     * If its day it calls day behavior
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        if(world == null) throw new NullPointerException("world cant be null");

        setDay(world.isDay());

        if (isDay()) dayBehavior(world);
        else nightBehavior(world);
    }

    /**
     * If the bunny is sleeping make it run the sleep method
     * If it does not have a burrow:
     * - It checks what takes less energy, a make a burrow, or go to an exiting one (If they are equal it goes to the closest one)
     * If it does have one it moves towards its burrow if it isnt in it, otherwise it does nothing
     */
    private void nightBehavior(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * - Set sleeping to false if its true, and call the grow method
     * - If its in a burrow, check if it can reproduce
     * -    It can reproduce if there are two rabbits in the burrow, and they both have enough energy
     * - If it cant reproduce it tries to dig more entries to the burrow (If it has enough energy)
     * - If it exits burrow
     * - If out of borrow moves towards grass
     */
    private void dayBehavior(World world) {
        if(sleeping){
            sleeping = false;
            grow();
        }
        if(inBurrow){
            //If its in a burrow, check if it can reproduce
            //     * -    It can reproduce if there are two rabbits in the burrow, and they both have enough energy
            //     * - If it cant reproduce it tries to dig more entries to the burrow (If it has enough energy)
            //     * - If it exits burrow
        } else {
            Location nearestGrass = findNearest(world,4,new Grass());
            if (nearestGrass != null) {
                if(distance(world, nearestGrass) == 0){
                    if(getHunger() < 100) eat((Grass) world.getNonBlocking(nearestGrass), world);
                }else {
                    moveTowards(nearestGrass, world);
                }
            }
        }
    }



    /**
     * Throws Main.IllegalOperationException if dig is called when the bunny already has a burrow
     * If The bunny has at least 25 energy:
     * - calls makeBurrow()
     */
    private void dig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * - Makes a burrow at the current location
     *      *      * - Initialises burrow variable to newly created burrow
     *      *      * - Subtracts 25 energy
     */
    private void makeBurrow() {

    }

    /**
     * Throws Main.IllegalOperationException if the bunny has no burrow
     * If the bunny does not have 50 energy return;
     * - calls makeBurrow()
     */
    private void expandBurrow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws Main.IllegalOperationException if the bunny is in a burrow or if the bunny has no burrow
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to true
     * removes the bunny from the world
     */
    private void enterBurrow(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws Main.IllegalOperationException if the bunny is not in a burrow or if the bunny has no burrow
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to false
     * Adds the bunny to the world in the location of a random burrow entry
     */
    private void exitBurrow(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Throws IllegalArgumentException if burrow is null
     * Throws Main.IllegalOperationException if the bunny has a burrow already
     * Initializes the burrow to the argument
     * @param burrow The burrow which the bunny should make its own
     */
    private void setBurrow(Burrow burrow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return true if bunny is in burrow and false if not
     */
    public boolean isInBurrow(){
        return inBurrow;
    }

    @Override
    public String getType(){
        return "rabbit";
    }

    @Override
    public Color getColor(){
        return Color.red;
    }
}
