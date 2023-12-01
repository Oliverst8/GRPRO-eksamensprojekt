package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.LinkedList;
import java.util.List;

public class Pack extends Community{
    private Den den;

    public Pack(){
        den = null;
    }

    public void createDen(Location denLocation){
        this.den = new Den(denLocation);
    }

    public Location getDenLocation(){
        return den.getLocation();
    }

    public Den getDen() {
        return den;
    }

    public Location findNearestMember(World world){
        List<Animal> wolvesAboveGround = new LinkedList<>(getMembers());
        if(den != null) wolvesAboveGround.removeAll(den.getMembers());
        return findNearestEntity(world, wolvesAboveGround);
    }

}
