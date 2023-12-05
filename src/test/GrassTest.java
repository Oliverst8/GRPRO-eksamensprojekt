package test;

import Main.Grass;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GrassTest {
    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 3; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    @Test
    void tryToSpreadGrassWhereThereIsNoEmptyTileExpectsEnergyToHaveIncreasedByPhotosynethesisAmount() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "grass");

        for(int i = 0; i < 3; i++) {
            ObjectFactory.generateOnMap(world, "grass");
        }

        double expectedEnergy = Math.min(100,grass.getEnergy() + 20);
        program.simulate();

        assertEquals(expectedEnergy,grass.getEnergy());
    }

    @Test
    void testIfGrassAges() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "grass");
        for(int i = 0; i < 20; i++){
            program.simulate();
        }
        
        assertEquals(1,grass.getAge());
    }

    @Test
    void testIfGrassHasLessEnergyTheOlderItGets() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "grass");
        grass.setEnergy(100);
        grass.grow();
        grass.addEnergy(100);

        assertEquals(90,grass.getEnergy());
    }

    @AfterEach
    void tearDown() {}
}