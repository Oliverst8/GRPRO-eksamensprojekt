package Main;

import itumulator.world.World;

import java.awt.*;

public class Berry extends Plant{

    private boolean containsBerries;

    private int timeSinceBerries;

    public Berry() {
        super(-2);
        containsBerries = true;
    }

    public boolean containsBerries(){
        return containsBerries;
    }

    public void growBerries(){
        if(energy > 50){
            timeSinceBerries = 0;
            containsBerries = true;
            removeEnergy(25);
        }
    }

    @Override
    public void die(World world){
        if(energy > 0 && containsBerries){
            containsBerries = false;
        } else {
            super.die(world);
        }
    }

    @Override
    public boolean isEatable(){
        return containsBerries;
    }

    @Override
    protected String getPNGPath() {
        StringBuilder path = new StringBuilder();

        path.append(getType());

        if(containsBerries) path.append("-berries");

        return path.toString();
    }

    @Override
    protected String getType() {
        return "bush";
    }

    @Override
    protected Color getColor() {
        return new Color(144,238,144);
    }

    @Override
    void dayBehavior(World world) {
        if(!containsBerries){
            timeSinceBerries++;
        }
        if(timeSinceBerries == 20){
            growBerries();
        }
    }

    @Override
    void nightBehavior(World world) {

    }
}
