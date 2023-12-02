package test;

import Main.Burrow;
import Main.Grass;
import Main.Rabbit;

import Main.Wolf;
import spawn.ObjectFactory;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class RabbitTest {

    Program program;
    World world;
    Rabbit rabbit;
    Burrow burrow;
    Rabbit rabbitInsideBurrow;

    /**
     * Calls rabbit constructor and creates a new Rabbit object
     */
    @BeforeEach
    void setUp() {
        int size = 20; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        rabbit = new Rabbit();

    }

    /**
     * Tests if NullPointerException is throwed when calling Rabbit.act() with Null
     * Asserts NullpointerException
     */
    @Test
    void testActWithNullArgumentExpectsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            rabbit.act(null);
        });
    }

    @Test
    void testConstructorAdultAge(){
        assertEquals(3, rabbit.getAdultAge());
    }

    /**
     * Tests isInBurrow
     * Should return false as initialised in the Rabbit constructor
     */
    @Test
    void testRabbitConstructorInBurrowExpectsFalse(){
        assertFalse(rabbit.isInBurrow());
    }

    @Test
    void testIfRabbitAgesAfterNight() {
        Rabbit rabbit = (Rabbit) initialiseRabbitOnWorld(new Location(0,0));
        for (int i = 0; i < 30; i++) {
            rabbit.setEnergy(100);
            program.simulate();
        }
        assertEquals(1,rabbit.getAge());
    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassWithOnly1GrassInWorldXValue() {
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));
        Grass grass = initialiseGrassOnWorld(new Location(1,1));
        grass.setEnergy(10);
        program.simulate();
        int actual = world.getLocation(rabbit1).getX();
        assertEquals(1, actual);

    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassWithOnly1GrassInWorldXValueTwice() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(0,0),new Location(3,3));
        rabbit.setHunger(99);
        program.simulate();
        program.simulate();
        assertEquals(2, world.getLocation(rabbit1).getX());
    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassWithOnly1GrassInWorldYValue() {
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));
        Grass grass = initialiseGrassOnWorld(new Location(1,1));
        grass.skipTurn();
        program.simulate();
        int acutal = world.getLocation(rabbit1).getY();
        assertEquals(1, acutal);

    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAlreadyOnGrassX() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(1,1));
        rabbit1.setHunger(99);
        program.simulate();
        assertEquals(1, world.getLocation(rabbit1).getX());

    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAndLoss10Energy() {
        Rabbit rabbit = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(2,2));
        int expectedEnergy = rabbit.getEnergy()-10;
        program.simulate();
        assertEquals(expectedEnergy, rabbit.getEnergy());

    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAlreadyOnGrassY() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(1,1));
        rabbit1.setHunger(99);
        program.simulate();
        assertEquals(1, world.getLocation(rabbit1).getY());

    }


    private Rabbit initialiseGrassAndRabbitOnWorld(Location rabbitLocation, Location grassLocation) {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, rabbitLocation, "rabbit");
        ObjectFactory.generateOnMap(world, grassLocation, "grass");
        return rabbit;
    }

    private Rabbit initialiseRabbitOnWorld(Location rabbitLocation) {
        Rabbit rabbit = null;
        try{
            rabbit = (Rabbit) ObjectFactory.generateOnMap(world, rabbitLocation, "rabbit");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return rabbit;
    }

    private Grass initialiseGrassOnWorld(Location grassLocation) {
        Grass grass = null;
        try{
            grass = (Grass) ObjectFactory.generateOnMap(world, grassLocation, "grass");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return grass;
    }

    @Test
    void testIfRabbitEatsGrassWhenStandingOnIt() {
        Rabbit rabbit = initialiseRabbitOnWorld(new Location(0,0));
        Grass grass = initialiseGrassOnWorld(new Location(0,0));
        double expectedHunger = Math.max(100, rabbit.getHunger()+(0.5*grass.getEnergy()));
        rabbit.setEnergy(30);
        program.simulate();
        assertEquals(expectedHunger, rabbit.getHunger());

    }

    @Test
    void testIfRabbitMovesToGrassAndEatsIt() {
        Rabbit rabbit = initialiseRabbitOnWorld(new Location(0,0));
        Grass grass = initialiseGrassOnWorld(new Location(2,2));
        double expectedHunger = Math.max(100, rabbit.getHunger()+(0.5*grass.getEnergy()));

        for (int i = 0; i < 3; i++) {
            program.simulate();
        }

        assertEquals(expectedHunger,rabbit.getHunger());
    }

    @Test
    void testDayBehaviorExpectsRabbitToExitHole(){
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        rabbitInsideBurrow.setHunger(99);
        rabbitInsideBurrow.setEnergy(60);
        program.simulate();
        assertEquals(0,burrow.getMembers().size());
    }
    @Test
    void testDayBehaviorExpectsRabbitsToExitHole(){
        Location burrowLocation = new Location(0,0);
        burrow = new Burrow(world, burrowLocation);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        rabbit.setHunger(99);
        rabbit.setEnergy(60);
        rabbit1.setHunger(99);
        rabbit1.setEnergy(60);
        program.simulate();
        int i = 0;
        if(burrow.getEntries().size() == 1) i++;
        if(burrow.getMembers().isEmpty()) i++;
        assertEquals(2,i);
    }
    @Test
    void testDayBehaviorExpectsRabbitToDigAnotherEntrance(){
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        rabbitInsideBurrow.setHunger(99);
        rabbitInsideBurrow.setEnergy(61);
        int expectedEntraceAmount = burrow.getEntries().size()+1;
        program.simulate();
        assertEquals(expectedEntraceAmount,burrow.getEntries().size());
    }

    @Test
    void testActDayBehaviorExpectsToMoveTowardsBurrowAndNotEnter() {

        Burrow burrow = new Burrow(world, new Location(2,2));
        Rabbit rabbit = new Rabbit(3, burrow, false);
        world.setTile(new Location(0,0),rabbit);
        rabbit.setHunger(100);
        program.simulate();

        assertEquals(new Location(1,1),world.getLocation(rabbit));
    }

    @Test
    void testActDayBehaviorExpectsToMoveTowardsBurrowAndEnter() {

        Burrow burrow = new Burrow(world, new Location(2,2));
        Rabbit rabbit = new Rabbit(3, burrow, false);
        world.setTile(new Location(0,0),rabbit);
        rabbit.setHunger(100);
        program.simulate();
        program.simulate();
        program.simulate();
        assertTrue(rabbit.isInBurrow());

    }

    @Test
    void testDayBehaviorWhereRabbitIsInBurrowExpectsRabbitToReproduce() {
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        rabbitInsideBurrow.setEnergy(100);
        rabbit2.setEnergy(100);
        int expected = world.getEntities().size()+1;
        program.simulate();
        assertEquals(expected,world.getEntities().size());
    }

    @Test
    void testDayBehaviorWhereRabbitIsInBurrowExpectsRabbitThatCallsToReproduceAndLoseEnergy() {
        burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        rabbit1.setEnergy(100);
        rabbit2.setEnergy(100);
        int expectedEnergy = rabbit1.getEnergy()-50;
        program.simulate();
        assertEquals(expectedEnergy, rabbit1.getEnergy());
    }

    @Test
    void testDayBehaviorWhereRabbitIsInBurrowExpectsRabbitThatDosentCallToReproduceAndLoseEnergy() {
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        Rabbit rabbit1 = rabbitInsideBurrow;
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "rabbit", 3, burrow, true);
        rabbit1.setEnergy(100);
        rabbit2.setEnergy(100);
        int expectedEnergy = rabbit2.getEnergy()-50;
        program.simulate();
        assertEquals(expectedEnergy, rabbit2.getEnergy());
    }

    @Test
    void testDaysBehaviorWhereRabbitsneedsToGoToGrassButThereIsAlreadyAnObjectExpectsRabbitToMoveOneCloser() {
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));
        Rabbit rabbit2 = initialiseRabbitOnWorld(new Location(1,1));
        Grass grass = initialiseGrassOnWorld(new Location(1,1));
        grass.setEnergy(10);
        rabbit2.setSkipTurn(true);
        program.simulate();
        assertEquals(new Location(1,0),world.getLocation(rabbit1));
    }
    @Test
    void testDaysBehaviorWhereRabbitsneedsToAvoidNearbyPredatorExpectsRabbitToMoveOneAwayFromLocationOfPredator() {
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(1,1));
        Location wolfLocation = new Location(0,0);
        Wolf wolf = null;
        try{
            wolf = (Wolf) ObjectFactory.generateOnMap(world,wolfLocation, "wolf");

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        wolf.setSkipTurn(true);
        program.simulate();
        assertEquals(new Location(2,2),world.getLocation(rabbit1));
    }

    /**
     * New rabbit gets 100 energy in constructor
     * Its night, has no burrow, digs new
     * digs cost 25 energy
     */
    @Test
    void testNightBehaviourWhereRabbitNeedsToSleepButHasNoBurrow(){

        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));
        world.setNight();
        program.simulate();
        assertEquals(75,rabbit1.getEnergy());
    }

    @Test
    void testNightBehaviourWhereRabbitHasBurrowButIsNotInside(){
        Burrow burrow = new Burrow(world, new Location(2,2));
        Rabbit rabbit = new Rabbit(3, burrow, false);
        world.setTile(new Location(0,0),rabbit);
        world.setNight();
        assertFalse(rabbit.isInBurrow());
        program.simulate(); //move towards burrow
        program.simulate(); //move towards burrow
        program.simulate(); //enter burrow
        assertTrue(rabbit.isInBurrow());

    }

    @Test
    void testNightBehaviorIfRabbitSleepsInsideBurrow(){
        Burrow burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbit = new Rabbit(3, burrow, false);
        world.setTile(new Location(0,0),rabbit);
        world.setNight();
        program.simulate(); //move towards burrow
        rabbit.setEnergy(90);
        program.simulate();
        assertEquals(100,rabbit.getEnergy()); //Animal.sleep() adds 10 energy gets called in nightBehaviour if sleeping is true
    }

    @Test
    void testThatRabbitCantExitBurrowFromBlockedEntrance(){
        Location testLocation = new Location(0,0);
        Burrow burrow = new Burrow(world, testLocation);
        Rabbit rabbit = new Rabbit(3, burrow, true);
        world.add(rabbit);
        rabbit.setHunger(99); // Under 100 so it wants to exit burrow
        rabbit.setEnergy(60); // Not more than 60 so it cant expand
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0)); //New rabbit that blocks entrance
        program.simulate(); //Wants to exit burrow
        program.simulate(); //Wants to exit burrow
        assertTrue(rabbit.isInBurrow());
    }

    @Test
    void testThatRabbitCantExitBurrowFromBlockedEntranceInTheBeginningThenBlockingObjectMoves(){

    }

    @AfterEach
    void tearDown() {
    }
}