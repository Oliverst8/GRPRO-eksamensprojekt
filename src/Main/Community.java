package Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Community {
    private List<Animal> members;

    public Community(){
        members = new LinkedList<>();
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
    public List<Animal> getMembers() {
        return members;
    }

    public List<Animal> getAdultMembers() {
        List<Animal> adultMembers = new ArrayList<>();

        for(Animal animal : members) {
            if(animal.getAge() >= animal.getAdultAge()) {
                adultMembers.add(animal);
            }
        }

        return adultMembers;
    }

}
