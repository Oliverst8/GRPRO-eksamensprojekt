package Main;

import itumulator.world.World;

import java.awt.*;

public class Wolf extends Animal{

    Pack pack;

    public Wolf() {
        super(new String[]{"meat"});
        pack = new Pack();
    }

    public Wolf(Pack pack){
        super(new String[]{"meat"});
        this.pack = pack;
    }


    public Pack getPack(){
        return pack;
    }


    @Override
    void produceOffSpring(World world) {

    }

    @Override
    protected String getType() {
        return "wolf";
    }

    @Override
    protected Color getColor() {
        return Color.gray;
    }

    @Override
    void dayBehavior(World world) {

    }

    @Override
    void nightBehavior(World world) {

    }
}
