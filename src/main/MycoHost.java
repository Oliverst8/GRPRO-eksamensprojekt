package main;

import itumulator.world.World;

public abstract class MycoHost extends Organism {
    Fungi fungi;

    public MycoHost(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
        fungi = null;
    }

    @Override
    public void act(World world) {
        if(isDying(world)) return;
        if(isInfected()) fungi.infectedBehavior(world, this);
        else super.act(world);
    }

    @Override
    public void die(World world) {
        if(isInfected()){
            fungi.hostDied(world, this);
        } else{
            super.die(world);
        }
    }

    /**
     * Sets the fungi that is infecting this host.
     * @param fungi the fungi that is infecting this host.
     */
    public void setInfected(Fungi fungi) {
        this.fungi = fungi;
    }

    /**
     * Gets the fungi that is infecting this host.
     * @return the fungi that is infecting this host.
     */
    public Fungi getFungi() {
        return fungi;
    }

    /**
     * Determines if this host is infected.
     * @return true if this host is infected, otherwise false.
     */
    public boolean isInfected() {
        return fungi != null;
    }
}
