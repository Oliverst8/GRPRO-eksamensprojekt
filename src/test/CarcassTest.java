package test;
import Main.Carcass;
import Main.Entity;
import Main.Rabbit;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spawn.ObjectFactory;

import static org.junit.jupiter.api.Assertions.*;
public class CarcassTest {

    Program program;
    World world;

    @BeforeEach
    void setUp(){
        int size = 20; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
    }

    @Test
    void testIfCarcussSpawnsWhenAnimalDies(){
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "rabbit");
        rabbit.setHealth(world, 0);
        program.simulate();
        assertEquals(world.getTile(location).getClass(), Carcass.class);
    }

    @Test
    void testGetEntityClassOverride() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "rabbit");
        rabbit.setEnergy(0);
        program.simulate();
        assertTrue(((Entity) world.getTile(location)).getEntityClass().equals(rabbit.getEntityClass()));
    }

    @Test
    void testCarcassDissapersAfter3Nights() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "rabbit");
        rabbit.setHealth(world,0);
        int adultAge = 3;
        for (int i = 0; i < adultAge*19; i++) {
            program.simulate();
            assertNotNull(world.getTile(location));
        }
        for(int i = 0; i < adultAge; i++){
            program.simulate();
        }
        assertNull(world.getTile(location));
    }
}
