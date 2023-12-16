package main;

import java.util.Set;

public interface Nest{
    /**
     * Adds a member to the nest.
     * @param member the member to add.
     */
    abstract void addMember(Animal member);

    abstract void removeMember(Animal member);

    abstract Set<Animal> getMembers();

    abstract Set<Animal> getAdultMembers();
}
