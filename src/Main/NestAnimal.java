package Main;

import itumulator.world.World;
import itumulator.world.Location;

public abstract class NestAnimal extends Animal {
    private boolean inNest = false;

    abstract Nest getNest();

    /**
     * @param defualtFoodChainValue
     */
    public NestAnimal(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
    }

    /**
     * Throws Main.IllegalOperationException if the NestAnimal is a nest or if the NestAnimal has No Nest
     * Throws IllegalArgumentException if world is null
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
            noNestBehavior(world);
        }
    }

    protected abstract void moveTowardsNest(World world);

    protected abstract void noNestBehavior(World world);

    /**
     * Throws Main.IllegalOperationException if the NestAnimal is not in its Nest
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

    protected abstract Location getExitLocation(World world);

    @Override
    public void dayBehavior(World world) {

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

    protected abstract void hungryBehavior(World world);

    protected abstract void inNestBehavior(World world);

    protected boolean reproduceBehavior(World world) {
        if (getEnergy() > 80 && getNest().getAdultMembers().size() >= 2) {
            for (Animal otherNestAnimal : getNest().getMembers()) {
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
