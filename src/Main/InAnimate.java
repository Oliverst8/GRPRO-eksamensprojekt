package Main;

import itumulator.world.World;

public abstract class InAnimate extends ObjectsOnMap{
    @Override
    protected String getPNGPath(){
        return getType();
    }

    @Override
    public void act(World world){

    }

}
