package test;

import Main.Burrow;
import Main.ObjectFactory;
import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        Rabbit rabbit = (Rabbit) ObjectFactory.generate(world, "rabbit");
        assertEquals(50,rabbit.getHunger());
    }

    @Test
    void testgenerateWithLocation() {
        Location location = new Location(1,1);
        Rabbit rabbit = (Rabbit) ObjectFactory.generate(world, location,"rabbit");
        assertEquals(new Location(1,1),world.getLocation(rabbit));
    }

    /**
     * https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
     */
    @Test
    void testGenerateWithUnknownClassname(){
        ByteArrayOutputStream placeholder = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(placeholder);
        PrintStream old = System.out;
        System.setOut(printStream);
        System.setOut(printStream);
        try {
            Rabbit rabbit = (Rabbit) ObjectFactory.generate(world,"UkendtKunstner");
        } catch (Exception e) {
            assertTrue(placeholder.toString().contains("Class not found: Main.Ukendtkunstner"));
        }


    }
    @AfterEach
    void tearDown() {
    }

}