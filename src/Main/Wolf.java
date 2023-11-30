package Main;

import itumulator.world.World;

import java.awt.*;

public class Wolf extends Animal{

    Pack pack;

    public Wolf() {
        super(new Class[]{Rabbit.class}, 1);
        pack = new Pack();
    }

    public Wolf(Pack pack){
        super(new Class[]{Rabbit.class}, 1);
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
