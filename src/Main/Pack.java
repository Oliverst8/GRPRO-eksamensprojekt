package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.LinkedList;
import java.util.List;

public class Pack extends Community{
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

    public Location findNearestMember(World world, Location wolfLocation){
        List<Animal> wolvesAboveGround = new LinkedList<>(getMembers());
        if(den != null) wolvesAboveGround.removeAll(den.getMembers());
        return findNearestEntity(world, wolfLocation, wolvesAboveGround);
    }

}
