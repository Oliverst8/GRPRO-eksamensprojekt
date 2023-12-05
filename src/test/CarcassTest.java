package test;
import Main.Carcass;
import Main.Entity;
import Main.Organism;
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
        rabbit.setHealth(100);
        rabbit.removeHealth(100,world);
        rabbit.setEnergy(100);
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
    void testCarcassDissapersAfter20Nights() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "rabbit");
        rabbit.setHealth(100);
        rabbit.removeHealth(100,world);
        program.simulate();
        Carcass carcass = (Carcass) world.getTile(location);

        for(int i = 0; i <= 100; i++){
            program.simulate();
            System.out.println(carcass.getEnergy());
        }

        assertNull(world.getTile(location));
    }
}
