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

    protected void drain(World world, MycoHost host){
        maxHealth += 5;
        maxEnergy += 5;
        addEnergy(5);
        addHealth(5);
        host.removeEnergy(5);
    }

    abstract void infectedBehavior(World world, MycoHost host);



}
