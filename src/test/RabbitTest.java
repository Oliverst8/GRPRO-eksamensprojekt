package test;

import Main.Grass;
import Main.ObjectFactory;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Main.Rabbit;

import static org.junit.jupiter.api.Assertions.*;

class RabbitTest {

    Program program;
    World world;
    Rabbit rabbit;

    /**
     * Calls rabbit constructor and creates a new Rabbit object
     */
    @BeforeEach
    void setUp() {
        int size = 3; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1000; // forsinkelsen mellem hver skridt af simulationen (i ms)
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
    void testActDayBehaviorExpectingToMoveTowardsGrassWithOnly1GrassInWorldXValue() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(0,0),new Location(1,1));
        program.simulate();
        assertEquals(1, world.getLocation(rabbit1).getX());

    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassWithOnly1GrassInWorldYValue() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(0,0),new Location(1,1));
        program.simulate();
        assertEquals(1, world.getLocation(rabbit1).getY());

    }

    @Test
    void testActDayBehaviorExpectingToMoveTowardsGrassAlreadyOnGrassX() {
        Rabbit rabbit1 = initialiseGrassAndRabbitOnWorld(new Location(1,1),new Location(1,1));
        rabbit1.setHunger(100);
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
        rabbit1.setHunger(100);
        program.simulate();
        assertEquals(1, world.getLocation(rabbit1).getY());

    }


    private Rabbit initialiseGrassAndRabbitOnWorld(Location rabbitLocation, Location grassLocation) {
        Rabbit rabbit = null;
        try{
            rabbit = (Rabbit) ObjectFactory.generate(world, rabbitLocation, "rabbit");
            ObjectFactory.generate(world, grassLocation, "grass");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return rabbit;
    }

    private Rabbit initialiseRabbitOnWorld(Location rabbitLocation) {
        Rabbit rabbit = null;
        try{
            rabbit = (Rabbit) ObjectFactory.generate(world, rabbitLocation, "rabbit");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return rabbit;
    }

    private Grass initialiseGrassOnWorld(Location grassLocation) {
        Grass grass = null;
        try{
            grass = (Grass) ObjectFactory.generate(world, grassLocation, "grass");
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

    @AfterEach
    void tearDown() {
    }
}