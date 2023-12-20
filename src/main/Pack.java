package main;

import java.util.Set;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public class Pack extends Community {
    private Den den;

    public Pack() {
        den = null;
    }

    /**
     * Creates a den at the location given as parameter.
     * @param world the world the den is in.
     * @param denLocation the location of the den.
     */
    public void createDen(World world, Location denLocation) {
        this.den = new Den(world, denLocation);
    }

    /**
     * Finds and returns the location of the den.
     * @param world the world the den is in.
     * @return the location of the den.
     */
    public Location getDenLocation(World world) {
        return den.getLocation(world);
    }

    /**
     * @return the den.
     */
    public Den getDen() {
        return den;
    }

    /**
     * Finds the nearest member of the pack to the wolf given as parameter.
     * @param world the world the wolf is in.
     * @param wolfLocation the location of the wolf.
     * @param wolf the wolf to find the nearest member of.
     * @return the location of the nearest member.
     */
    public Location findNearestMember(World world, Location wolfLocation, Wolf wolf){
        Set<Animal> wolvesAboveGround = new HashSet<>(getMembers());
        wolvesAboveGround.remove(wolf);
        if(den != null) wolvesAboveGround.removeAll(den.getMembers());

        Entity nearestMember = Utility.findNearest(world, wolfLocation, wolvesAboveGround);

        if (nearestMember == null) return null;

        return world.getLocation(nearestMember);
    }
}
