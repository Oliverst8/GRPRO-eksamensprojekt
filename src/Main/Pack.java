package Main;

import itumulator.world.Location;

public class Pack extends Community{
    Den den;

    public void createDen(Location denLocation){
        this.den = new Den(denLocation);
    }

    public Location getDenLocation(){
        return den.getLocation();
    }
}
