package Main;

import itumulator.world.World;

public abstract class NestAnimal extends Animal{

    private boolean inNest = false;


    /**
     * @param defualtFoodChainValue
     */
    public NestAnimal(int defualtFoodChainValue) {
        super(defualtFoodChainValue);
    }

    /**
     * Throws Main.IllegalOperationException if the NestAnimal is a nest or if the NestAnimal has No Nest
     * Throws IllegalArgumentException if world is null
     * Sets inBurrow to true
     * removes the bunny from the world
     * When entering a burrow the rabbit goes to sleep
     */
    protected void enterNest(World world){

        if(isInNest()) throw new IllegalOperationException("Cant enter nest, when " + this + " is already in its den");
        if(getNest() == null) throw new IllegalOperationException("Cant enter nest when " + this + " has no nest");

        setInNest(true);
        getNest().addMember(this);
        world.remove(this);
    }

    abstract Nest getNest();

    public boolean isInNest(){
        return inNest;
    }

    public void setInNest(Boolean inNest){
        this.inNest = inNest;
    }


}
