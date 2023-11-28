package test;

import Main.Grass;


import spawn.ObjectFactory;

import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class GrassTest {
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
    void tryToSpreadGrassWhereThereIsNoEmptyTileExpectsEnergyToHaveIncreasedByPhotosynethesisAmount() {
        int size = 2; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "grass");
        ObjectFactory.generateOnMap(world, "grass");
        ObjectFactory.generateOnMap(world, "grass");
        ObjectFactory.generateOnMap(world, "grass");
        double expectedEnergy = Math.min(100,grass.getEnergy()+10);
        program.simulate();
        assertEquals(expectedEnergy,grass.getEnergy());

    }

    @Test
    void grassCanSpreadSuccesfully(){
        int size = 2; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "grass");
        int totalEntities = world.getEntities().size();
        grass.setEnergy(100);
        program.simulate();
        int totalEntities2 = world.getEntities().size();
        assertTrue(totalEntities<totalEntities2);
    }

    @Test
    void canGrassDecay(){
        int size = 2; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "grass");
        int totalEntities = world.getEntities().size();
        grass.setEnergy(10);
        world.setNight();
        program.simulate();
        int totalEntities2 = world.getEntities().size();
        assertTrue(totalEntities>totalEntities2);
    }
    
    @Test
    void grassTest() {
    }

    @AfterEach
    void tearDown() {
    }
}