package Main;

import itumulator.world.Location;

public class Den extends Community{

    Hole den;

    public Den(Location denLocation){
        den = new Hole(denLocation,"hole");
    }

    public Location getLocation(){
        return den.getLocation();
    }

}
