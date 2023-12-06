package Main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.awt.*;

public class Ghoul extends Organism implements Fungi{

    public Ghoul() {
        super(-2);
    }

    @Override
    protected String getType() {
        return null;
    }

    @Override
    protected Color getColor() {
        return null;
    }

    @Override
    public void findNewHost(World world, MycoHost oldHost) {
        MycoHost nearestHost = (MycoHost) Helper.findNearest(world, this, 3, MycoHost.class);
        if(nearestHost == null) return;

        nearestHost.setInfected(this);
    }

    @Override
    public void hostDied(World world, MycoHost host) {
        Location hostLocation = world.getLocation(host);

        world.delete(host);

        world.setTile(hostLocation, this);
    }

    @Override
    public void dayBehavior(World world) {
        behavior(world);
    }

    @Override
    public void nightBehavior(World world) {
        behavior(world);
    }

    public void behavior(World world){
        if(!world.contains(this)) return;
    }
}
