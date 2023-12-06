package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.HashSet;
import java.util.Set;

public abstract class Fungi extends Organism{

    /**
     * @param defualtFoodChainValue
     */
    public Fungi(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
    }

    abstract void hostDied(World world, MycoHost host);

    abstract MycoHost findNewHost(World world, Location location);

    protected Set<MycoHost> filterNonInfectedMycoHosts(Set<Entity> entities){
        Set<MycoHost> nonInfected = new HashSet<>();
        for(Entity entity : entities){
            MycoHost host = (MycoHost) entity;
            if(!host.isInfected()) nonInfected.add(host);
        }
        return nonInfected;
    }



}
