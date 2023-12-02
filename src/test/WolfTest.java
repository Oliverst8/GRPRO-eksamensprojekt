package test;

import static org.junit.jupiter.api.Assertions.*;

import Main.Helper;
import Main.Rabbit;
import Main.Wolf;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spawn.ObjectFactory;

public class WolfTest {

    Program program;
    World world;
    Wolf wolf;

    @BeforeEach
    void setUp(){
        int size = 20; // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 1; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)
        program = new Program(size, display_size, delay); // opret et nyt program
        world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!

        wolf = new Wolf();
    }

    @Test
    void testWolfConstructorWithoutPackExpectsNewPack() {
        assertNotNull(new Wolf().getPack());
    }

    @Test
    void testWolfConstructorWithPackExpectsNewPack() {
        assertEquals(wolf.getPack(), new Wolf(wolf.getPack(), 3, false).getPack());
    }

    @Test
    void testDayBehaviorExpectsWolfToCreateAndEnterDen() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        wolf.setHunger(100);
        program.simulate();
        assertTrue(wolf.getInDen());
    }

    @Test
    void testDayBehaviorExpectsFirstWolfToCreateDenAndSecondToEnter() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "wolf", wolf.getPack(), 3, false);
        wolf.setHunger(100);
        wolf2.setHunger(100);
        program.simulate();
        program.simulate();
        assertTrue(wolf.getInDen());
        assertTrue(wolf2.getInDen());
    }

    @Test
    void testDaybehaviorInDenExpectsWolfToReproduce(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf", 5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "wolf", wolf.getPack(), 5, false);
        wolf.setHunger(100);
        wolf2.setHunger(100);
        program.simulate();
        program.simulate();
        program.simulate();
        assertEquals(3, wolf.getPack().getDen().getMembers().size());
    }

    @Test
    void testDaybehaviorWolfToExitDen(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        wolf.setHunger(100);
        program.simulate();
        wolf.setHunger(50);
        program.simulate();
        assertFalse(wolf.getInDen());
    }

    @Test
    void testDaybehaviorExpectsWolfToStayInDen(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        wolf.setHunger(100);
        program.simulate();
        program.simulate();
        assertTrue(wolf.getInDen());
    }

    @Test
    void testDaybehaviorExpectsWolfToGoToDen(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        wolf.setHunger(100);
        program.simulate();
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "wolf", wolf.getPack(), 3, false);
        wolf2.setHunger(100);
        program.simulate();
        program.simulate();
        program.simulate();
        assertTrue(wolf2.getInDen());
    }

    @Test
    void testDayBehaviorExpectsWolfToRunAwayFromOtherWolfInSideWorld(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "wolf");
        program.simulate();
        assertEquals(new Location(2,2),world.getLocation(wolf2));
    }

    @Test
    void testDayBehaviorExpectsToStandStillEvenThoNextToOtherPack(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "wolf");
        program.simulate();
        assertEquals(new Location(0,0),world.getLocation(wolf));
    }

    @Test
    void testDayBehaviorExpectsWolfsToCreateHuntingPackAndMoveTowardsRabbit() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "wolf", wolf.getPack(), 3, false);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (3,3),"Rabbit");
        rabbit.skipTurn();
        double currentDistWolf1 = Helper.distance(world.getLocation(wolf),world.getLocation(rabbit));
        double currentDistWolf2 = Helper.distance(world.getLocation(wolf2),world.getLocation(rabbit));

        program.simulate();
        program.simulate();

        assertTrue(Helper.distance(world.getLocation(wolf),world.getLocation(rabbit)) < currentDistWolf1-1);
        assertTrue(Helper.distance(world.getLocation(wolf2),world.getLocation(rabbit)) < currentDistWolf2-1);
    }

    @Test
    void testDayBehaviorExpectsWolfToEatCarcuss(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (1,1),"Rabbit");
        rabbit.die(world);
        program.simulate();
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        wolf.setHunger(50);
        double expectedEnergy = Math.min(100, wolf.getEnergy()+rabbit.getEnergy());
        program.simulate();

        assertEquals(expectedEnergy, wolf.getEnergy());

    }

    @Test
    void testDayBehaviorExpectsWolfToEatCarcussWhereEveryoneInHuntingPackGetsEnergy(){
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (1,1),"Rabbit");
        rabbit.die(world);
        program.simulate();
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,1), "wolf");
        wolf.setHunger(50);
        wolf2.setHunger(50);
        program.simulate();
        double expectedEnergyWolf1 = Math.min(100, wolf.getEnergy()+rabbit.getEnergy());
        double expectedEnergyWolf2 = Math.min(100, wolf2.getEnergy()+rabbit.getEnergy());

        assertEquals(expectedEnergyWolf1, wolf.getEnergy());
        assertEquals(expectedEnergyWolf2, wolf2.getEnergy());

    }

    @Test
    void testDayBehaviorExpectsWolfToAttack() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf", 5);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (3,3),"Rabbit");
        rabbit.skipTurn();
        //Do something
    }

    @Test
    void testThatWolfsIsRemovedFromPackWhenDead() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "wolf", 5);
        wolf.die(world);
        program.simulate();
        assertEquals(0, wolf.getPack().getMembers().size());
    }

    @AfterEach
    void tearDown() {
    }

}
