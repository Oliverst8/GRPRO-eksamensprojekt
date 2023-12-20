package main;

import java.util.Set;

public interface Nest{
    /**
     * Adds a member to the nest
     * @param member the member to add.
     */
    abstract void addMember(Animal member);

    /**
     * Removes a member from the next
     * @param member the member to remove
     */
    abstract void removeMember(Animal member);

    /**
     * gets Members from the nest
     * @return a Set containing all the members of the nest
     */
    abstract Set<Animal> getMembers();

    /**
     * Gets adult members from the nest
     * @return a Set containing all the adult members of the nest
     */
    abstract Set<Animal> getAdultMembers();
}
