package test;

import Main.Wolf;
import Main.Grass;
import Main.Burrow;
import Main.Rabbit;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testConstructorAdultAge() {
        assertEquals(3, rabbit.getAdultAge());
    }

    /**
     * Tests isInBurrow
     * Should return false as initialised in the Rabbit constructor
     */
    @Test
    void testRabbitConstructorInBurrowExpectsFalse() {
        assertFalse(rabbit.isInNest());
    }

    @Test
    void testIfRabbitAgesAfterNight() {
        Rabbit rabbit = (Rabbit) initialiseRabbitOnWorld(new Location(0,0));
        
        world.setNight();

        for (int i = 0; i < 11; i++) {
            rabbit.setEnergy(100);
            program.simulate();
        }

        assertEquals(1, rabbit.getAge());
    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassWithOnly1GrassInWorldXValue() {
        Grass grass = initialiseGrassOnWorld(new Location(1,1));
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));

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
        Grass grass = initialiseGrassOnWorld(new Location(1,1));
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));

        grass.skipTurn();
        program.simulate();

        int actual = world.getLocation(rabbit1).getY();
        assertEquals(1, actual);
    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAlreadyOnGrassX() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(1,1));

        rabbit1.setHunger(99);
        program.simulate();

        assertEquals(1, world.getLocation(rabbit1).getX());
    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAndLoseEnergy() {
        Rabbit rabbit = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(2,2));

        int previousEnergy = rabbit.getEnergy();
        program.simulate();

        assertTrue(rabbit.getEnergy() < previousEnergy);
    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAlreadyOnGrassY() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(1,1));

        rabbit1.setHunger(99);
        program.simulate();

        assertEquals(1, world.getLocation(rabbit1).getY());
    }


    private Rabbit initialiseGrassAndRabbitOnWorld(Location rabbitLocation, Location grassLocation) {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, rabbitLocation, "Rabbit");
        ObjectFactory.generateOnMap(world, grassLocation, "Grass");
        return rabbit;
    }

    private Rabbit initialiseRabbitOnWorld(Location rabbitLocation) {
        return (Rabbit) ObjectFactory.generateOnMap(world, rabbitLocation, "Rabbit");
    }

    private Grass initialiseGrassOnWorld(Location grassLocation) {
        return (Grass) ObjectFactory.generateOnMap(world, grassLocation, "Grass");
    }

    @Test
    void testIfRabbitEatsGrassWhenStandingOnIt() {
        Grass grass = initialiseGrassOnWorld(new Location(0,0));
        Rabbit rabbit = initialiseRabbitOnWorld(new Location(0,0));

        double expectedHunger = Math.max(100, rabbit.getHunger()+(0.5*grass.getEnergy()));
        rabbit.setEnergy(30);
        program.simulate();

        assertEquals(expectedHunger, rabbit.getHunger());
    }

    @Test
    void testIfRabbitMovesToGrassAndEatsIt() {
        Grass grass = initialiseGrassOnWorld(new Location(2,2));
        Rabbit rabbit = initialiseRabbitOnWorld(new Location(0,0));

        double expectedHunger = Math.max(100, rabbit.getHunger()+(0.5*grass.getEnergy()));

        for (int i = 0; i < 3; i++) {
            grass.skipTurn();
            program.simulate();
        }

        assertEquals(expectedHunger,rabbit.getHunger());
    }

    @Test
    void testDayBehaviorExpectsRabbitToExitHole() {
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        rabbitInsideBurrow.setHunger(99);
        rabbitInsideBurrow.setEnergy(60);
        program.simulate();
        assertEquals(0,burrow.getMembers().size());
    }
    @Test
    void testDayBehaviorExpectsRabbitsToExitHole() {
        Location burrowLocation = new Location(0,0);
        burrow = new Burrow(world, burrowLocation);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
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
    void testDayBehaviorExpectsRabbitToDigAnotherEntrance() {
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
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
        assertTrue(rabbit.isInNest());
    }

    @Test
    void testDayBehaviorWhereRabbitIsInBurrowExpectsRabbitToReproduce() {
       //rykket til KravTest K1_2e
    }

    @Test
    void testDayBehaviorWhereRabbitIsInBurrowExpectsRabbitThatCallsToReproduceAndLoseEnergy() {
        burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        rabbit1.setEnergy(100);
        rabbit2.setEnergy(100);
        int expectedEnergy = rabbit1.getEnergy()-50;
        program.simulate();
        assertEquals(expectedEnergy, rabbit1.getEnergy());
    }

    @Test
    void testDayBehaviorWhereRabbitIsInBurrowExpectsRabbitThatDosentCallToReproduceAndLoseEnergy() {
        burrow = new Burrow(world, new Location(0,0));
        rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        Rabbit rabbit1 = rabbitInsideBurrow;
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        rabbit1.setEnergy(100);
        rabbit2.setEnergy(100);
        int expectedEnergy = rabbit2.getEnergy()-50;
        program.simulate();

        assertEquals(expectedEnergy, rabbit2.getEnergy());
    }

    @Test
    void testDaysBehaviorWhereRabbitsneedsToGoToGrassButThereIsAlreadyAnObjectExpectsRabbitToMoveOneCloser() {
        Grass grass = initialiseGrassOnWorld(new Location(2,2));
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));
        Rabbit rabbit2 = initialiseRabbitOnWorld(new Location(1,1));

        world.setCurrentLocation(world.getLocation(rabbit1));
        rabbit1.act(world);

        assertEquals(new Location(1,0),world.getLocation(rabbit1));
    }

    @Test
    void testDaysBehaviorWhereRabbitsneedsToAvoidNearbyPredatorExpectsRabbitToMoveOneAwayFromLocationOfPredator() {
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(1,1));
        Location wolfLocation = new Location(0,0);
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world,wolfLocation, "Wolf");

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
    void testNightBehaviourWhereRabbitNeedsToSleepButHasNoBurrow() {
        Rabbit rabbit1 = initialiseRabbitOnWorld(new Location(0,0));

        world.setNight();
        program.simulate();

        assertEquals(75,rabbit1.getEnergy());
    }

    @Test
    void testNightBehaviourWhereRabbitHasBurrowButIsNotInside() {
        Burrow burrow = new Burrow(world, new Location(2,2));
        Rabbit rabbit = new Rabbit(3, burrow, false);
        world.setTile(new Location(0,0),rabbit);
        world.setNight();

        assertFalse(rabbit.isInNest());
        program.simulate(); //move towards burrow
        program.simulate(); //move towards burrow
        program.simulate(); //enter burrow
        assertTrue(rabbit.isInNest());
    }

    @Test
    void testNightBehaviorIfRabbitSleepsInsideBurrow() {
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
    void testThatRabbitCantExitBurrowFromBlockedEntrance() {
        Location testLocation = new Location(0,0);
        Burrow burrow = new Burrow(world, testLocation);
        Rabbit rabbit = new Rabbit(3, burrow, true);
        world.add(rabbit);
        rabbit.setHunger(99); // Under 100 so it wants to exit burrow
        rabbit.setEnergy(60); // Not more than 60 so it cant expand
        initialiseRabbitOnWorld(testLocation); //New rabbit that blocks entrance
        
        for(Location location : world.getEmptySurroundingTiles(testLocation)) {
            initialiseRabbitOnWorld(location);
        }

        program.simulate(); //Wants to exit burrow
        program.simulate(); //Wants to exit burrow
        assertTrue(rabbit.isInNest());
    }

    @Test
    void testThatRabbitIsRemovedFromBorrowWhenDead() {
        Burrow burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbit = new Rabbit(3, burrow, false);

        world.setTile(new Location(0,0),rabbit);
        rabbit.setEnergy(0);
        program.simulate();

        assertEquals(0,burrow.getMembers().size());
    }

    @AfterEach
    void tearDown() {}
}