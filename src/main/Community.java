package main;

import java.util.Set;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public abstract class Community {
    private Set<Animal> members;

    public Community(){
        members = new HashSet<>();
    }

    /**
     * Add a member to the Community
     * @param member
     */
    public void addMember(Animal member) {
        members.add(member);
    }
    
    /**
     * Removes parameter member from the list of members
     */
    public void removeMember(Animal member) {
        members.remove(member);
    }

    /**
     * @return the list of members in the community
     */
    public Set<Animal> getMembers() {
        return members;
    }

    /**
     * @return the list of adult members in the community
     */
    public Set<Animal> getAdultMembers() {
        Set<Animal> adultMembers = new HashSet<>();

        for(Animal animal : members) {
            if(animal.getAge() >= animal.getAdultAge()) {
                adultMembers.add(animal);
            }
        }

        return adultMembers;
    }

    /**
     * Checks if the community contains an animal.
     * @param animal the animal to check.
     * @return true if the community contains the animal given as parameter.
     */
    public boolean contains(Organism animal) {
        return members.contains(animal);
    }

    /**
     * Find nearest entity from a set of entities.
     * @param world the world to look in.
     * @param location the location to look around.
     * @param enitities the set of entities to look through.
     * @return the location of the nearest entity.
     */
    protected Location findNearestEntity(World world, Location location, Set<? extends Entity> enitities) {
        Location closestEntityLocation = null;
        double minDist = Double.MAX_VALUE;

        for(Entity entity : enitities) {
            double distance = Helper.distance(location, world.getLocation(entity));
            if(minDist > distance) {
                minDist = distance;
                closestEntityLocation = world.getLocation(entity);
            }
        }
        return closestEntityLocation;
    }
}
