package Main;

import itumulator.world.World;

public abstract class MycoHost extends Organism{
    /**
     * @param defualtFoodChainValue
     */
    public MycoHost(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
        fungi = null;
    }

    Fungi fungi;

    @Override
    public void die(World world){
        if(isInfected()){
            fungi.hostDied(world, this);
        } else{
            super.die(world);
        }
    }

    public void setInfected(Fungi fungi){
        this.fungi = fungi;
    }

    public boolean isInfected(){
        return fungi != null;
    }

    abstract void infectedBehavior();

}
