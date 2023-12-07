package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Set;

public class Cordyceps extends Fungi{

    private Animal currentHost;

    /**
     * @param defualtFoodChainValue
     */
    public Cordyceps(Animal host) {
        super(-3);
        currentHost = host;
    }

    @Override
    public void hostDied(World world, MycoHost host) {
        Animal newHost = (Animal) findNewHost(world, world.getLocation(host));

        world.delete(host);

        if(newHost != null){
            newHost.setInfected(this);
            currentHost = newHost;
        }
        else world.delete(this);
    }

    @Override
    public MycoHost findNewHost(World world, Location location) {
        Set<Entity> surroundingEntities = Helper.getEntities(world, location, 3);
        surroundingEntities.remove((Entity) world.getTile(location));
        surroundingEntities = Helper.filterByClass(surroundingEntities, Animal.class);

        Set<MycoHost> potentialHosts = filterNonInfectedMycoHosts(surroundingEntities);
        if(potentialHosts.isEmpty()) return null;

        return (MycoHost) Helper.findNearest(world, location, potentialHosts);
    }

    @Override
    void dayBehavior(World world) {
        behavior(world);
    }

    @Override
    void nightBehavior(World world) {
        behavior(world);
    }


    private void behavior(World world){
        drain(world, currentHost);
    }

    @Override
    public void infectedBehavior(World world, MycoHost host){
        currentHost = (Animal) host;
        Set<Entity> surrondingEntities = Helper.getEntities(world, world.getLocation(currentHost), 3);
        surrondingEntities = Helper.filterByClass(surrondingEntities, currentHost.getClass());

        surrondingEntities.retainAll(filterNonInfectedMycoHosts(surrondingEntities));

        Entity closetAnimalOfSameSpecies = Helper.findNearest(world, world.getLocation(currentHost), surrondingEntities);
        if(closetAnimalOfSameSpecies == null) return;


        currentHost.moveTowards(world, world.getLocation(closetAnimalOfSameSpecies));
    }

    public Animal getCurrentHost(){
        return currentHost;
    }

}
