package main;

import itumulator.world.World;
import itumulator.world.Location;

import error.IllegalOperationException;
import error.CantReproduceException;

public abstract class NestAnimal extends Animal {
    private boolean inNest = false;

    public NestAnimal(int defaultStrength) {
        super(defaultStrength);
    }

    /**
     * @return the nest that the animal belongs to.
     */
    abstract Nest getNest();

    /**
     * Moves the animal towards its nest.
     * @param world the world the animal is in.
     */
    protected abstract void moveTowardsNest(World world);

    /**
     * The behavior of the animal when it has no nest.
     * @param world the world the animal is in.
     */
    protected abstract void noNestBehavior(World world);

    /**
     * Get a random exit location from the nest.
     * @param world the world the animal is in.
     * @return a random exit location from the nest.
     */
    protected abstract Location getExitLocation(World world);

    /**
     * The behavior of the animal when it is hungry.
     * @param world the world the animal is in.
     */
    protected abstract void hungryBehavior(World world);

    /**
     * The behavior of the animal when it is in its nest.
     * @param world the world the animal is in.
     */
    protected abstract void inNestBehavior(World world);

    @Override
    protected void dayBehavior(World world) {
        if (isDying(world)) return;

        if (isInNest()) {
            inNestBehavior(world);
            return;
        }

        if (getHunger() >= 100) {
            goToNest(world);
        } else {
            hungryBehavior(world);
        }
    }

    @Override
    protected void nightBehavior(World world) {
        if(isInNest()) sleeping = true;

        if(sleeping){
            sleep();
            return;
        }

        goToNest(world);
    }

    public boolean isInNest() {
        return inNest;
    }

    public void setInNest(Boolean inNest) {
        this.inNest = inNest;
    }

    protected void goToNest(World world) {
        if (getNest() != null) {
            moveTowardsNest(world);
        } else {
            if(world.containsNonBlocking(world.getCurrentLocation())){
                wander(world);
                return;
            }
            noNestBehavior(world);
        }
    }

    /**
     * Throws Error.IllegalOperationException if the NestAnimal is a nest or if the NestAnimal has No Nest
     * Throws IllegalArgumentException if the world is null
     * Sets inBurrow to true
     * removes the bunny from the world
     * When entering a burrow the rabbit goes to sleep
     */
    protected void enterNest(World world) {

        if (isInNest()) throw new IllegalOperationException("Cant enter nest, when " + this + " is already in its den");
        if (getNest() == null) throw new IllegalOperationException("Cant enter nest when " + this + " has no nest");

        setInNest(true);
        getNest().addMember(this);
        world.remove(this);
    }

    /**
     * Throws Error.IllegalOperationException if the NestAnimal is not in its Nest
     * Sets inNest to false
     * Adds the NestAnimal to the world in the location exitLocation
     */
    protected void exitNest(World world) {
        if (!isInNest()) throw new IllegalOperationException("Cant exit a burrow, if its not in one");

        Location freeLocation = getExitLocation(world);
        if (freeLocation == null) return;

        setInNest(false);
        getNest().removeMember(this);
        world.setTile(freeLocation, this);
    }

    protected boolean reproduceBehavior(World world) {
        if(!isAdult()) return false;
        if (getEnergy() > 80 && getNest().getAdultMembers().size() >= 2) {
            for (Animal otherNestAnimal : getNest().getAdultMembers()) {
                if (otherNestAnimal != this && otherNestAnimal.getEnergy() > 80) {
                    try {
                        reproduce(world, this, otherNestAnimal);
                    } catch (CantReproduceException e) {
                        e.printInformation();
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
