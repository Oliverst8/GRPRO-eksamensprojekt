package Main;

import itumulator.world.World;

import java.util.HashSet;
import java.util.Set;

public abstract class MycoHost extends Organism{

    Fungi fungi;

    /**
     * @param defualtFoodChainValue
     */
    public MycoHost(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
        fungi = null;
    }

    @Override
    public void act(World world){
        if(isInfected()) fungi.infectedBehavior(world, this);
        else super.act(world);
    }

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



}
