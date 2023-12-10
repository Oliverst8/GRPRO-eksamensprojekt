package Main;

import java.util.Set;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public class Pack extends Community {
    private Den den;

    public Pack() {
        den = null;
    }

    public void createDen(World world, Location denLocation) {
        this.den = new Den(world, denLocation);
    }

    public Location getDenLocation(World world) {
        return den.getLocation(world);
    }

    public Den getDen() {
        return den;
    }

    public Location findNearestMember(World world, Location wolfLocation, Wolf wolf){
        Set<Animal> wolvesAboveGround = new HashSet<>(getMembers());
        wolvesAboveGround.remove(wolf);
        if(den != null) wolvesAboveGround.removeAll(den.getMembers());
        return findNearestEntity(world, wolfLocation, wolvesAboveGround);
    }
}
