package Main;

import java.awt.Color;

import itumulator.world.World;

public class Bear extends Animal {

    public Bear(String[] canEat) {
        super(canEat);
    }

    @Override
    void produceOffSpring(World world) {
        throw new UnsupportedOperationException("Unimplemented method 'produceOffSpring'");
    }

    @Override
    void dayBehavior(World world) {
        if(sleeping) {
            sleeping = false;
            grow();
        }

        
    }

    @Override
    void nightBehavior(World world) {
        throw new UnsupportedOperationException("Unimplemented method 'nightBehavior'");
    }

    @Override
    protected String getType() {
        return "bear";
    }

    @Override
    protected Color getColor() {
        return Color.lightGray;
    }
}
