package Main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.awt.*;
import java.util.Set;

public class Ghoul extends Fungi implements Cloneable{

    public Ghoul() {
        super(-2);
    }

    @Override
    protected String getType() {
        return null;
    }

    @Override
    protected Color getColor() {
        return new Color(37,42,4);
    }

    @Override
    public void hostDied(World world, MycoHost host) {
        Location hostLocation = world.getLocation(host);

        world.delete(host);

        world.setTile(hostLocation, this);
    }

    @Override
    public MycoHost findNewHost(World world, Location location) {
        Set<Entity> surroundingEntities = Helper.getEntities(world, location, 3);
        surroundingEntities.remove((Entity) world.getTile(location));
        surroundingEntities = Helper.filterByClass(surroundingEntities, Carcass.class);

        Set<MycoHost> potentialHosts = filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return null;

        return (MycoHost) Helper.findNearest(world, location, potentialHosts);
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
        spread(world);
    }



    private void spread(World world){
        MycoHost newHost = findNewHost(world, world.getLocation(this));
        if(newHost == null) return;
        try{
            Ghoul clone = (Ghoul) this.clone();
            newHost.setInfected(clone);
        } catch(CloneNotSupportedException e){
            throw new RuntimeException("Failed to clone ghoul", e);
        }
    }
}
