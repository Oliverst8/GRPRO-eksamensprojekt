package main;

import java.awt.Color;

import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;

public class Wolf extends NestAnimal {
    private final Pack pack; //The pack the wolf is part off
    private Pack huntingPack; //If the wolf is not part of a huntingpack it is null, otherwise this is the huntingpack

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolfs, default food chain value to 1
     * creates a pack for the wolf
     * calls initialise with a age of 0
     */
    public Wolf() {
        super(1);
        pack = new Pack();
        pack.addMember(this);
        initialise(0);
    }

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolfs, default food chain value to 1
     * @param age the starting age of the wolf
     * calls initialise with a age of the parameter
     */
    public Wolf(int age) {
        super(1);
        pack = new Pack();
        pack.addMember(this);
        initialise(age);
    }

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolves, default food chain value to 1
     * @param pack the pack that the wolf belongs to
     * calls initialize with an age of the parameter
     */
    public Wolf(Pack pack) {
        super(1);
        this.pack = pack;
        pack.addMember(this);
        initialise(age);
    }

    /**
     * Creates a new wolf
     * Makes the wolf capable of eating rabbits
     * Sets the wolves, default food chain value to 1
     * @param pack the pack the wolf is going to be part off
     * @param age the starting age of the wolf
     * @param inDen weather or not, the wolf is in its den
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
     * adds the following to a list of what the wolf can eat:
     * bears, rabbits, carcasses
     */
    @Override
    protected void setupCanEat() {
        addCanEat(Bear.class);
        addCanEat(Rabbit.class);
        addCanEat(Carcass.class);
        addCanEat(Egg.class);
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
            ((Wolf) wolf).eatAlone(food);
        }

        food.die(world);
    }

    /**
     * @return the type of animal this is, which is wolf
     */
    @Override
    public String getType() {
        return "wolf";
    }

    /**
     * @return the default color for this animal, which is gray for wolf
     */
    @Override
    public Color getColor() {
        return Color.gray;
    }

    /**
     *
     * @return The food chain value of the wolf
     * If the wolf is in a hunting pack the food chain value is equal to its size, otherwise it return the default value for wolves
     */
    @Override
    public int getStrength() {
        if(huntingPack == null) return super.getStrength();
        else return huntingPack.getMembers().size();
    }

    /**
     * When called, calls super.die which kills the wolf
     * If its a member of a huntingpack, it gets removed from the list of members of it
     * If its a member of a pack, it gets removed from the list of members of it
     * @param world current world
     */
    @Override
    public void die(World world) {
        super.die(world);
        if(getHuntingPack() != null) getHuntingPack().removeMember(this);
        if(pack != null) pack.removeMember(this);
    }

    /**
     * If the wolf is in a hunting packs, it calls the super movetowards twice otherwise once
     * @param location the location the wolf is going towards
     * @param world the world the wolf is in
     */
    @Override
    protected void moveTowards(World world, Location location) {
        if(huntingPack != null) super.moveTowards(world, location, 2, this);
        else super.moveTowards(world, location,1, this);
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
     * hunt function
     * if huntingpack is null it calls, super.hunt from animal
     * if huntingpack exist
     * returns false if the prey is a part of the same pack as the wolf
     * else all of the members of the wolves huntingpack will hunt the prey
     *
     * @param world the world which the animal is on
     * @return true
     */
    @Override
    protected boolean hunt(World world) {

        if(huntingPack != null){
            Organism prey = findPrey(world, 4);

            // Skip if a prey is from the same pack
            if(prey instanceof Wolf){
                if(((Wolf) prey).getPack().equals(getPack())) return false;
            }

            for(Animal wolf : huntingPack.getMembers()){
                world.setCurrentLocation(world.getLocation(wolf));
                wolf.huntPrey(world, prey);
                if(prey == null || !world.contains(prey)) break;
            }

            world.setCurrentLocation(world.getLocation(this));
            skipHuntingPacksTurn();
            setSkipTurn(false);
            return true;
        } else{
            return super.hunt(world);
        }
    }

    /**
     * @return the pack the wolf is part off
     */
    public Pack getPack() {
        return pack;
    }

    /**
     * skips turn, skips calls for one tick for all huntingpackmembers
     */
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
     * @param huntingPack the huntingpack the wolf is going to be part off
     */
    public void setHuntingPack(Pack huntingPack) {
        this.huntingPack = huntingPack;
        huntingPack.addMember(this);
    }

    /**
     * Makes a wolf eat something without it dying
     * @param food the food to be consumed
     */
    public void eatAlone(Organism food) {
        if(canIEat(food.getEntityClass())){
            addHunger(0.5 * food.getEnergy());
        }
    }

    /**
     * @return the nest of the pack that the wolf belongs to
     */
    public Nest getNest() {
        return pack.getDen();
    }

    /**
     * Hungrybehaviour for wolf
     * if it can attack a nearby wolf it returns
     * if the wolf is a part of an active huntingpack it hunts
     * if there createorjoinhuntingpack in a 3 distance radius isnt true, find the nearestwolf of the same pack
     * If there is a wolf within the raidus of 3 it moves towards it
     * if not it hunts
     * in the end the wolf will try to run away from other wolfs and their den
     * @param world the world the wolf is in
     */
    protected void hungryBehavior(World world) {
        if(attackNearbyWolf(world)) return;

        if(huntingPack != null) {
            hunt(world);
            return;
        }

        if(!(createOrJoinHuntingPack(world, 3))) {
            Location nearestWolfLocation = pack.findNearestMember(world, world.getLocation(this),this);
            if(nearestWolfLocation != null) moveTowards(world, nearestWolfLocation);
            else hunt(world);
        }

        evadeOtherWolfs(world);
    }

    /**
     * If the wolf can reproduce it returns
     * if the wolfs hunger is below 100 it exits the nest
     * @param world the world the wolf is in
     */
    protected void inNestBehavior(World world) {
        if(reproduceBehavior(world)) return;
        if(getHunger() < 100) {
            exitNest(world);
        }
    }

    /**
     * Removes the wolf from its huntingpack
     * then makes the wolf go to its nest
     */
    protected void goToNest(World world) {
        if(huntingPack != null){
            huntingPack.removeMember(this);
            huntingPack = null;
        }

        super.goToNest(world);
    }

    /**
     * nonestbehaviour is called if the wolf has no nest,
     * it creates a den for the pack and enters it
     * @param world the world the wolf is in
     */
    protected void noNestBehavior(World world) {
        pack.createDen(world, world.getCurrentLocation());
        enterNest(world);
    }

    /**
     * Makes the wolf move towards the nest that belongs to the pack that the wolf is a member of
     * @param world the world the wolf is in
     */
    protected void moveTowardsNest(World world) {
        moveTowards(world, pack.getDenLocation(world));
        if(Utility.distance(world.getLocation(this), pack.getDenLocation(world)) < 2) enterNest(world);
    }

    /**
     * Gets the location that the wolf can exit to from its nest.
     * Adds all empty tiles surrounding the exit and the exit itself to a list, if the exit itself also is empty.
     * @param world the world the wolf is in
     * @return null if the list is empty, else a random location from the list
     */
    protected Location getExitLocation(World world) {
        List<Location> exitLocations = new ArrayList<>(world.getEmptySurroundingTiles(pack.getDenLocation(world)));
        if(world.isTileEmpty(pack.getDenLocation(world))) exitLocations.add(pack.getDenLocation(world));

        if(exitLocations.isEmpty()) return null;

        Random random = new Random();
        return exitLocations.get(random.nextInt(exitLocations.size()));
    }

    /**
     * Sets the starting values of the wolf
     * huntingpack to null
     * adultage to 5
     * initialises the following
     * age, setinnest(false),maxenergy, energy as maxenergy, maxhealth, health as maxhealth, strength
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
        damage = 100;
    }

    /**
     * gets the surroundingtiles, if the surroundingtiles contains a wolf that isnt a part of a huntingpack already
     * it gets added to a list, if the other wolf has a huntingpack the wolf gets added to that huntingpack and returns true.
     * if the list is empty, it returns false,
     * every wolf in the list gets added to the huntingpack and returns true
     * @param world the world the wolf is in
     * @param radius the radius to check for another wolfing
     * @return true if the wolf is a joins or creates a huntingpack, false if not
     */
    private boolean createOrJoinHuntingPack(World world, int radius) {
        Set<Location> surroundingLocations = world.getSurroundingTiles(radius);
        List<Wolf> foundWolfes = new ArrayList<>();

        for(Location tile : surroundingLocations){
            Object tileObject = world.getTile(tile);
            if(tileObject instanceof Wolf wolf){
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

    /**
     * check surrounding tiles for wolves, if the surrounding wolf is not a part of the same pack as the wolf it gets added to the list of targets.
     * @param world the world the wolf is in
     * @return true if the wolf attacks a nearby wolf, false if not
     */
    private boolean attackNearbyWolf(World world) {
        Set<Entity> neighbors = Utility.getEntities(world, world.getLocation(this), 1);

        Set<Entity> nearbyWolves = Utility.filterByClass(neighbors, getClass());

        Set<Wolf> targetWolves = new HashSet<>();

        for(Entity otherWolf : nearbyWolves) {
            if(!((Wolf) otherWolf).getPack().equals(getPack())) {
                targetWolves.add((Wolf) otherWolf);
            }
        }

        // If there are no nearby wolves, return.
        if(targetWolves.isEmpty()) return false;

        Random random = new Random();

        int randomIndex = random.nextInt(targetWolves.size());
        Wolf randomWolf = (Wolf) targetWolves.toArray()[randomIndex];

        attack(world, randomWolf);

        return true;
    }

    /**
     * finds the nearest wolf from within a 3 distance radius in the wolrd
     * if there is a wolf from within that radius that isnt in the same pack as the wolf that the function is called from
     * it moves away from it
     * @param world the world the wolf is in
     */
    private void evadeOtherWolfs(World world) {
        Wolf nearestWolf = (Wolf) findNearestPrey(world, 3, Wolf.class);
        if(nearestWolf != null) {
            if (!(nearestWolf.getPack()).equals(getPack())) {
                moveAwayFrom(world, world.getLocation(nearestWolf));
                return;
            }
        }

        WolfHole nearestWolfHole = (WolfHole) findNearestPrey(world, 3, WolfHole.class);
        if(nearestWolfHole != null) {
            if(this.getPack().getDen()!=null) {
                if(!pack.getDen().getLocation(world).equals(nearestWolfHole.getLocation(world))) {
                    moveAwayFrom(world, nearestWolfHole.getLocation(world));
                    return;
                }
            }
            moveAwayFrom(world, nearestWolfHole.getLocation(world));
        }

    }
}
