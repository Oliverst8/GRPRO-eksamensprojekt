package main;

import java.awt.Color;

import java.util.Set;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Ghoul extends Organism implements Fungi {

    /**
     * Creates a new ghoul;
     */
    public Ghoul() {
        super(-2);
        adultAge = 1;
        maxEnergy = 500;
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

        if(getEnergy() > 20) world.setTile(hostLocation, this);
    }

    @Override
    public MycoHost findNewHost(World world, Location location) {
        Set<Entity> surroundingEntities = Utility.getEntities(world, location, 3);
        surroundingEntities.remove((Entity) world.getTile(location));
        surroundingEntities = Utility.filterByClass(surroundingEntities, Carcass.class);

        Set<MycoHost> potentialHosts = filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return null;

        return (MycoHost) Utility.findNearest(world, location, potentialHosts);
    }

    @Override
    public void infectedBehavior(World world, MycoHost host) {
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

    @Override
    public void die(World world) {
        Location location = world.getLocation(this);

        super.die(world);

        if(!world.containsNonBlocking(location)) ObjectFactory.generateOnMap(world, location, "Grass");
    }

    @Override
    public void drain(World world, MycoHost host) {
        maxHealth += 5;
        maxEnergy += 5;
        addEnergy(5);
        addHealth(5);
        host.removeEnergy(5);
    }

    /**
     * Determines the behavior of the fungi.
     * @param world the world which the fungi are in.
     */
    private void behavior(World world) {
        if(!world.isOnTile(this)) return;

        spread(world);
        removeEnergy(10);
        removeHealth(10, world);
    }

    /**
     * Spreads the fungi to a new host.
     * @param world the world which the fungi are in.
     */
    private void spread(World world) {
        MycoHost newHost = findNewHost(world, world.getLocation(this));
        if(newHost == null) return;

        Ghoul newFungi = (Ghoul) ObjectFactory.generateOffMap(world, "Ghoul");
        newHost.setInfected(newFungi);
    }

    /**
     * Spreads the fungi to a new host.
     * @param world the world which the fungi are in.
     * @param host the host which the fungi are in.
     */
    private void spread(World world, MycoHost host) {
        MycoHost newHost = findNewHost(world, world.getLocation(host));
        if(newHost == null) return;

        Ghoul newFungi = (Ghoul) ObjectFactory.generateOffMap(world, "Ghoul");
        newHost.setInfected(newFungi);
    }
}
