package main;

import itumulator.world.World;

public abstract class MycoHost extends Organism {
    Fungi fungi;

    /**
     * @param defualtFoodChainValue
     */
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

    public void setInfected(Fungi fungi) {
        this.fungi = fungi;
    }

    public Fungi getFungi() {
        return fungi;
    }

    public boolean isInfected() {
        return fungi != null;
    }
}
