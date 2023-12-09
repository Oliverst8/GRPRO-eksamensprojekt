package test;

import Main.Grass;
import Main.Consumable;

import itumulator.world.World;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PlantTest {
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
    void testPlantConstructor() {
        Grass grass = new Grass();

        assertInstanceOf(Consumable.class, grass);
    }
}