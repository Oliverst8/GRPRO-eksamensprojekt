package Main;

import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import java.awt.*;
import java.util.Set;

public class Ghoul extends Fungi implements Spawnable {

    public Ghoul() {
        super(-2);
        adultAge = 1;
    }

    @Override
    public String getType() {
        return "fungi";
    }

    @Override
    public Color getColor() {
        return new Color(37,42,4);
    }

    @Override
    public String getPNGPath() {
        StringBuilder path = new StringBuilder();

        path.append(getType());
        if(maxHealth > 150) path.append("-large");
        else path.append("-small");

        return path.toString();
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
    void infectedBehavior(World world, MycoHost host) {
        drain(world, host);
        if(!world.isOnTile(host)) return;
        spread(world, host);
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
        if(!world.isOnTile(this)) return;
        spread(world);
        removeEnergy(10);
        removeHealth(10, world);
    }



    private void spread(World world){
        MycoHost newHost = findNewHost(world, world.getLocation(this));
        if(newHost == null) return;

        Ghoul newFungi = (Ghoul) ObjectFactory.generateOffMap(world, "Ghoul");
        newHost.setInfected(newFungi);
    }

    private void spread(World world, MycoHost host){
        MycoHost newHost = findNewHost(world, world.getLocation(host));
        if(newHost == null) return;

        Ghoul newFungi = (Ghoul) ObjectFactory.generateOffMap(world, "Ghoul");
        newHost.setInfected(newFungi);
    }
}
