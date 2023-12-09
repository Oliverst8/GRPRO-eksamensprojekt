package test;

import itumulator.world.World;
import itumulator.executable.Program;

import Main.Berry;

import spawn.ObjectFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
public class BerryTest {

    Program program;
    World world;

    @BeforeEach
    void setup(){
        int size = 2; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void testIfBushLosesItsBerriesAndStaysOnMap() {
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");
        assertTrue(berry.containsBerries());
        berry.die(world);
        assertFalse(berry.containsBerries());
        assertTrue(world.contains(berry));
    }

    @Test
    void testIfBerryBushDissapearsWhenDying() {
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");
        berry.die(world);
        berry.die(world);
        assertFalse(world.contains(berry));
    }

    @Test
    void testIfBushGrowsBerriesAfter2Days(){
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");
        berry.die(world);
        for(int i = 0; i < 2*19; i++){
            program.simulate();
            assertFalse(berry.containsBerries());
        }
        program.simulate();
        program.simulate();
        assertTrue(berry.containsBerries());
    }
}
