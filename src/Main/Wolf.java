package Main;

import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Wolf extends Animal{

    private Pack pack;

    private Pack huntingPack;

    private boolean inDen;

    public Wolf() {
        super(new Class[]{Rabbit.class}, 1);
        pack = new Pack();
        initialise();
    }

    public Wolf(Pack pack){
        super(new Class[]{Rabbit.class}, 1);
        this.pack = pack;
        initialise();
    }

    private void initialise() {
        huntingPack = null;
        adultAge = 5;
        age = 5;
    }

    public void setInDen(boolean inDen){
        this.inDen = inDen;
    }

    public boolean getInDen(){
        return inDen;
    }


    public Pack getPack(){
        return pack;
    }


    @Override
    void produceOffSpring(World world) {
        throw new UnsupportedOperationException("produce offspring in Wolf is not implemented yet");
    }

    @Override
    protected String getType() {
        return "wolf";
    }

    @Override
    protected Color getColor() {
        return Color.gray;
    }

    @Override
    protected void moveTowards(Location location, World world){
        if(huntingPack != null) moveTowards(location, world, 2, this);
        else moveTowards(location,world,1, this);
    }

    /**
     * If the wolf is in its den
     * - If the the wolf has more then 90 energy it tries to reproduce
     * - If the wolf cant reproduce and its hunger is less then 100it leaves the den
     * - If the wolfs hunger is maxed it goes to its den
     * - If the wolf sees another wolf close by it moves away
     * - If the wolf is in a hunting party it hunts
     * - If it dosent hunt it checks if it can join a close by hunting party/create one
     * Otherwise it goes to another wolf, if there are non other above ground in the pack it hunts alone
     * @param world
     */
    @Override
    void dayBehavior(World world) {
        if(getInDen()){
            if(getEnergy() > 80 && pack.getDen().getAdultMembers().size() >= 2){
                for(Animal otherWolf : pack.getDen().getMembers()){
                    if(otherWolf != this && otherWolf.getEnergy() > 80) {
                        try{
                            reproduce(world, this, otherWolf);
                        } catch (cantReproduceException e){
                            e.printInformation();
                        }
                        return;
                    }
                }
            }
            if(getHunger() < 100) {
                exitDen(world);
                return;
            }

        } else{
            if(getHunger() == 100) {
                goToDen(world);
                return;
            }
            Location nearestWolfLocation = findNearest(world, 3, Wolf.class);
            if(nearestWolfLocation != null){
                Wolf nearestWolf = (Wolf) world.getTile(nearestWolfLocation);
                if (!(nearestWolf.getPack()).equals(getPack())) {
                    moveAwayFrom(world, nearestWolfLocation);
                    return;
                }
            }
            if(huntingPack != null){
                Organism prey = findPrey(world, 4);
                for(Animal wolf : huntingPack.getMembers()){
                    wolf.huntPrey(world, prey);
                    wolf.skipTurn();
                }
                setSkipTurn(false);
                return;
            }
            if(!(createOrJoinHuntingPack(world, 3))) {
                Location nearestWolf = pack.findNearestMember(world);
                if(nearestWolf != null) moveTowards(nearestWolf, world);
                else hunt(world);
            }
        }
    }

    private boolean createOrJoinHuntingPack(World world, int radius) {
        Set<Location> surroundingLocations = world.getSurroundingTiles(radius);
        List<Wolf> foundWolfes = new ArrayList<>();
        for(Location tile : surroundingLocations){
            Object tileObject = world.getTile(tile);
            if(tileObject instanceof Wolf){
                Wolf wolf = (Wolf) tileObject;
                if(pack.equals(wolf.getPack())){
                    if(wolf.getHuntingPack() != null){
                        setHuntingPack(wolf.huntingPack);
                        return true;
                    }  else{
                        foundWolfes.add(wolf);
                    }
                }
            }
        }
        if(foundWolfes.isEmpty()) return false;
        setHuntingPack(new Pack());
        for(Wolf wolf : foundWolfes){
            wolf.setHuntingPack(huntingPack);
            wolf.skipTurn();
        }
        setSkipTurn(false);
        return true;
    }

    public Pack getHuntingPack() {
        return huntingPack;
    }

    public void setHuntingPack(Pack huntingPack) {
        this.huntingPack = huntingPack;
        huntingPack.addMember(this);
    }

    @Override
    public void eat(Organism food, World world){
        if(huntingPack == null) {
            super.eat(food, world);
            return;
        }
        for(Animal wolf : huntingPack.getMembers()){
            ((Wolf)wolf).eatAlone(food, world);
            food.die(world);
        }
    }

    public void eatAlone(Organism food, World world){
        if(canIEat(food.getClass())){
            addHunger(0.5 * food.getEnergy());
        }
    }

    /**
     * Makes the wolf go towards its den and enter it
     */
    private void goToDen(World world) {
        if(pack.getDen() == null) {
            pack.createDen(world.getCurrentLocation());
            enterDen(world);
            return;
        }
        huntingPack = null;
        if(pack.getDenLocation().equals(world.getCurrentLocation())) {
            enterDen(world);
            return;
        }
        moveTowards(pack.getDenLocation(), world);
        if(pack.getDenLocation().equals(world.getCurrentLocation())) enterDen(world);
    }

    private void exitDen(World world) {

        List<Location> exitLocations = new LinkedList<>(world.getEmptySurroundingTiles(pack.getDenLocation()));
        if(world.isTileEmpty(pack.getDenLocation())) exitLocations.add(0,pack.getDenLocation());

        if(exitLocations.isEmpty()) return;

        setInDen(false);
        pack.getDen().removeMember(this);
        world.setTile(exitLocations.get(0),this);
    }

    private void enterDen(World world){
        if(!(pack.getDenLocation().equals(world.getCurrentLocation()))) throw new IllegalOperationException("Cant enter den, when wolf is not stading on it");
        if(getInDen()) throw new IllegalOperationException("Cant enter den, when the wolf is already in its den");

        setInDen(true);
        pack.getDen().addMember(this);
        world.remove(this);
    }

    @Override
    void nightBehavior(World world) {

    }
}
