package Main;

import java.awt.Color;

import itumulator.world.World;

public class Berry extends Plant{

    private boolean containsBerries;

    private int timeSinceBerries;

    public Berry() {
        super(-2);
        containsBerries = true;
    }

    public boolean containsBerries() {
        return containsBerries;
    }

    public void growBerries() {
        if(energy > 50){
            timeSinceBerries = 0;
            containsBerries = true;
            removeEnergy(25);
        }
    }

    @Override
    public void die(World world) {
        if(energy > 0 && containsBerries) {
            containsBerries = false;
        } else {
            super.die(world);
        }
    }

    @Override
    public boolean isEatable() {
        return containsBerries;
    }

    @Override
    public String getPNGPath() {
        StringBuilder path = new StringBuilder();

        path.append(getType());

        if(containsBerries) path.append("-berries");

        return path.toString();
    }

    @Override
    public String getType() {
        return "bush";
    }

    @Override
    public Color getColor() {
        return new Color(144,238,144);
    }

    @Override
    public void dayBehavior(World world) {
        if(!containsBerries) {
            timeSinceBerries++;
        }
        if(timeSinceBerries == 20) {
            growBerries();
        }
    }

    @Override
    public void nightBehavior(World world) {}
}
