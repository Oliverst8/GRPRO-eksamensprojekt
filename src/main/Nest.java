package main;

import java.util.Set;

public interface Nest{
    abstract void addMember(Animal member);

    abstract void removeMember(Animal member);

    abstract Set<Animal> getMembers();

    abstract Set<Animal> getAdultMembers();
}
