package Main;

import java.awt.Color;

import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Wolf extends NestAnimal {
    private Pack pack; //The pack the wolf is part off

    private Pack huntingPack; //If the wolf is not part of a huntingpack it is null, otherwise this is the huntingpack

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolfs, default food chain value to 1
     * creates a pack for the wolf
     * calles initialise with a age of 0
     */
    public Wolf() {
        super(1);
        pack = new Pack();
        initialise(0);
    }

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolfs, default food chain value to 1
     * @param age the starting age of the wolf
     * calles initialise with a age of the parameter
     */
    public Wolf(int age) {
        super(1);
        pack = new Pack();
        initialise(age);
    }

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolfs, default food chain value to 1
     * @param pack the pack the wolf is going to be part off
     * @param age the starting age of the wolf
     * @param inDen weather or not the wolf is in its den
     */
    public Wolf(Pack pack, int age, boolean inDen) {
        super(1);
        this.pack = pack;
        pack.addMember(this);
        initialise(age);
        if(inDen){
            setInNest(true);
            pack.getDen().addMember(this);
        }
    }

    /**
     * Sets the starting values of the wolf
     * huntingpack to null
     * adultage to 5
     * @param age the age of the wolf
     */
    private void initialise(int age) {
        huntingPack = null;
        adultAge = 5;
        this.age = age;
        setInNest(false);
        maxEnergy = 200;
        energy = maxEnergy;
        maxHealth = 200;
        health = maxHealth;
        strength = 100;
    }

    /**
     * @return the pack the wolf is part off
     */
    public Pack getPack() {
        return pack;
    }

    /**
     * Creates a new wolf with the same pack as the parent, and a age of 5 in a den
     * @param world the wolf is part off
     */
    @Override
    protected void produceOffSpring(World world) {
        ObjectFactory.generateOffMap(world, "Wolf",this.getPack(), 0, true);
    }

    /**
     * @return the type of animal this is
     */
    @Override
    public String getType() {
        return "wolf";
    }

    /**
     * @return the default color for this animal
     */
    @Override
    public Color getColor() {
        return Color.gray;
    }

    /**
     * If the wolf is in a hunting packs it calls the super movetowards twice otherwise once
     * @param location the location the wolf is going towards
     * @param world the world the wolf is in
     */
    @Override
    protected void moveTowards(World world, Location location) {
        if(huntingPack != null) super.moveTowards(world, location, 2, this);
        else super.moveTowards(world, location,1, this);
    }

    @Override
    void setupCanEat() {
        canEat.add(Rabbit.class);
        canEat.add(Bear.class);
        canEat.add(Carcass.class);
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
    public void dayBehavior(World world) {
        super.dayBehavior(world);
    }

    protected void hungryBehavior(World world) {
        Wolf nearestWolf =  (Wolf) findNearestPrey(world, 3, Wolf.class);
        if(nearestWolf != null){
            if (!(nearestWolf.getPack()).equals(getPack())) {
                moveAwayFrom(world, world.getLocation(nearestWolf));
                return;
            }
        }

        if(huntingPack != null) {
            hunt(world);
            return;
        }

        if(!(createOrJoinHuntingPack(world, 3))) {
            Location nearestWolfLocation = pack.findNearestMember(world, world.getLocation(this));
            if(nearestWolfLocation != null) moveTowards(world, nearestWolfLocation);
            else hunt(world);
        }
    }

    @Override
    protected void hunt(World world) {
        if(huntingPack != null){
            Organism prey = findPrey(world, 4);

            for(Animal wolf : huntingPack.getMembers()){
                world.setCurrentLocation(world.getLocation(wolf));
                wolf.huntPrey(world, prey);
                if(!world.contains(prey)) break;
            }

            world.setCurrentLocation(world.getLocation(this));
            skipHuntingPacksTurn();
            setSkipTurn(false);
        } else{
            super.hunt(world);
        }
    }

    protected void inNestBehavior(World world) {
        if(reproduceBehavior(world)) return;
        if(getHunger() < 100) {
            exitNest(world);
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

    public void skipHuntingPacksTurn() {
        for (Animal wolf: huntingPack.getMembers()) {
            wolf.skipTurn();
        }
    }

    /**
     * @return the hunting pack of the wolf
     */
    public Pack getHuntingPack() {
        return huntingPack;
    }

    /**
     * Sets the huntingpack of the wolf, and adds the wolf to the member list of the pack
     * @param huntingPack
     */
    public void setHuntingPack(Pack huntingPack) {
        this.huntingPack = huntingPack;
        huntingPack.addMember(this);
    }

    /**
     * If the wolf is not in a hunting pack, it eats normally
     * If the wolf is in a hunting pack, everyone of the hunting pack gets energy
     * @param food the food to be eaten
     * @param world the world the wolves are in
     */
    @Override
    public void eat(World world, Organism food) {
        if(huntingPack == null) {
            super.eat(world, food);
            return;
        }

        for(Animal wolf : huntingPack.getMembers()){
            ((Wolf) wolf).eatAlone(world, food);
        }

        food.die(world);
    }

    /**
     * Makes a wolf eat something without it dying
     * @param food the food to be consumed
     * @param world the world the wolf is in
     */
    public void eatAlone(World world, Organism food) {
        if(canIEat(food.getEntityClass())){
            addHunger(0.5 * food.getEnergy());
        }
    }

    /**
     * Removes the wolf from its huntingpack
     * Calls go to nest
     */
    protected void goToNest(World world) {
        if(huntingPack != null){
            huntingPack.removeMember(this);
            huntingPack = null;
        }

        super.goToNest(world);
    }

    protected void noNestBehavior(World world) {
        pack.createDen(world, world.getCurrentLocation());
        enterNest(world);
    }

    protected void moveTowardsNest(World world) {
        moveTowards(world, pack.getDenLocation(world));
        if(pack.getDenLocation(world).equals(world.getCurrentLocation())) enterNest(world);
    }

    protected Location getExitLocation(World world) {
        List<Location> exitLocations = new ArrayList<>(world.getEmptySurroundingTiles(pack.getDenLocation(world)));
        if(world.isTileEmpty(pack.getDenLocation(world))) exitLocations.add(pack.getDenLocation(world));

        if(exitLocations.isEmpty()) return null;

        Random random = new Random();
        return exitLocations.get(random.nextInt(exitLocations.size()));
    }

    public Nest getNest() {
        return pack.getDen();
    }

    /**
     *
     * @return The food chain value of the wolf
     * If the wolf is in a hunting pack the food chain value is equal to its size, otherwise it return the default value for wolves
     */
    @Override
    public int getFoodChainValue() {
        if(huntingPack == null) return super.getFoodChainValue();
        else return huntingPack.getMembers().size();
    }

    /**
     * If its inside of its den, then sleep true and calls sleep
     * If not it calls goToDen where it movestowards its den or digs one
     * @param world
     */
    @Override
    public void nightBehavior(World world) {
        if(isInNest()) sleeping = true;
        if(sleeping){
            sleep();
            return;
        }
        goToNest(world);
    }

    @Override
    public void die(World world) {
        super.die(world);
        if(getHuntingPack() != null) getHuntingPack().removeMember(this);
        if(pack != null) pack.removeMember(this);
    }
}
