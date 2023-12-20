package main;

import java.util.Set;
import java.util.HashSet;

import itumulator.world.World;
import itumulator.world.Location;

public abstract class Community {
    private final Set<Animal> members;

    public Community(){
        members = new HashSet<>();
    }

    /**
     * Add a member to the Community
     * @param member the member to add
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
     * @return the set of members in the community
     */
    public Set<Animal> getMembers() {
        return members;
    }

    /**
     * @return the set of adult members in the community
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
}
