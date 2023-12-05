package test;

import Main.Burrow;
import Main.Rabbit;
import Main.Wolf;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import spawn.ObjectFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObjectFactoryTest {

    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 20; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
    }

    @Test
    void testgenerateWithoutLocation() {
        assertInstanceOf(Rabbit.class, ObjectFactory.generateOnMap(world, "Rabbit"));
    }

    @Test
    void testgenerateWithLocation() {
        Location location = new Location(1,1);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");
        assertEquals(new Location(1,1),world.getLocation(rabbit));
    }

    @Test
    void generateWith3ConstructorArgumentsExpectsRabbit(){
        Burrow burrow = new Burrow(world, new Location(0,0));
        assertInstanceOf(Rabbit.class, ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true));
    }

    @Test
    void generateWith0ConstructorArgumentsExpectsRabbit(){
        assertInstanceOf(Rabbit.class, ObjectFactory.generateOffMap(world, "Rabbit") );
    }

    @Test
    void generateWith0ConstructorArgumentsExpectsWolf(){
        assertInstanceOf(Wolf.class, ObjectFactory.generateOffMap(world, "Wolf") );
    }

    @AfterEach
    void tearDown() {
    }
}