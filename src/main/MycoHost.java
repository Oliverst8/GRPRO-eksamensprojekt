package main;

import itumulator.world.World;

public abstract class MycoHost extends Organism {
    Fungi fungi;

    /**
     * Creates a new host with the given default strength.
     * @param defaultStrength the default strength of this host.
     */
    public MycoHost(int defaultStrength) {
        super(defaultStrength);
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
     * Sets the fungi that are infecting this host.
     * @param fungi the fungi that are infecting this host.
     */
    public void setInfected(Fungi fungi) {
        this.fungi = fungi;
    }

    /**
     * Gets the fungi that are infecting this host.
     * @return the fungi that are infecting this host.
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
