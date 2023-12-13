package test;

import java.util.Objects;

import main.Wolf;
import main.Rabbit;
import main.Carcass;

import spawn.ObjectFactory;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WolfTest {
    Program program;
    World world;
    Wolf wolf;

    @BeforeEach
    void setUp(){
        int size = 4; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();

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
    void testWolfgetFoodChainValue() {
        assertEquals(1, wolf.getFoodChainValue());
    }

    @Test
    void testWolfgetFoodChainValueInPack() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf.getPack(), 5, false);
        wolf.setHunger(0);
        wolf2.setHunger(0);
        program.simulate();

        assertEquals(2, wolf.getFoodChainValue());
    }

    @Test
    void testDayBehaviorExpectsWolfToCreateAndEnterDen() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        wolf.setHunger(100);
        world.setCurrentLocation(world.getLocation(wolf));
        wolf.act(world); //dig nest enter den
        wolf.setHunger(99); //below 0 will exit den
        wolf.act(world); //out of den
        world.move(wolf,new Location(0,1));

        if(Objects.equals(world.getLocation(wolf), new Location(0, 1))){
            wolf.setHunger(100);
            wolf.act(world);
        }
        assertTrue(wolf.isInNest());
    }

    @Test
    void testDayBehaviorExpectsFirstWolfToCreateDenAndSecondToEnter() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf.getPack(), 3, false);
        wolf.setHunger(100);
        wolf2.setHunger(100);

        program.simulate();

        assertTrue(wolf.isInNest());
        assertTrue(wolf2.isInNest());
    }

    @Test
    void testDaybehaviorInDenExpectsWolfToReproduce() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf.getPack(), 5, false);
        wolf.setHunger(100);
        wolf2.setHunger(100);

        program.simulate();
        program.simulate();

        assertEquals(3, wolf.getPack().getDen().getMembers().size());
    }

    @Test
    void testDaybehaviorWolfToExitDen() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        wolf.setHunger(100);
        program.simulate();
        wolf.setHunger(50);
        program.simulate();

        assertFalse(wolf.isInNest());
    }

    @Test
    void testDaybehaviorExpectsWolfToStayInDen() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        wolf.setHunger(100);
        program.simulate();
        program.simulate();

        assertTrue(wolf.isInNest());
    }

    @Test
    void testDaybehaviorExpectsWolfToGoToDen() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        wolf.setHunger(100);
        program.simulate();
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf", wolf.getPack(), 3, false);
        wolf2.setHunger(100);
        program.simulate();
        program.simulate();
        program.simulate();

        assertTrue(wolf2.isInNest());
    }

    @Test
    void testDayBehaviorExpectsWolfToRunAwayFromOtherWolfInSideWorld() {
        ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf");

        program.simulate();

        assertEquals(new Location(3,3),world.getLocation(wolf2));
    }

    @Test
    void testDayBehaviorExpectsToStandStillEvenThoNextToOtherPack() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf");
        program.simulate();

        assertEquals(new Location(0,0),world.getLocation(wolf));
    }

    @Test
    void testDayBehaviorExpectsWolfsToCreateHuntingPackAndMoveTowardsRabbit() {
        Wolf wolf1 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf", wolf1.getPack(), 3, false);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (3,3),"Rabbit");

        rabbit.skipTurn();
        program.simulate();
        rabbit.skipTurn();
        program.simulate();

        Location wolf1Location = world.getLocation(wolf1);
        Location wolf2Location = world.getLocation(wolf2);
        Location predictedWolf1Location1 = new Location(2,1);
        Location predictedWolf1Locaiton2 = new Location(2,2);
        boolean isAtPredictedLocation = false;

        if(Objects.equals(wolf1Location, predictedWolf1Location1) || Objects.equals(wolf1Location, predictedWolf1Locaiton2)) {
            isAtPredictedLocation = true;
        }
        Location predictedWolf2Location = new Location(3,2);

        assertTrue(isAtPredictedLocation);
        assertEquals(predictedWolf2Location,wolf2Location);
    }

    @Test
    void testDayBehaviorExpectsWolfToEatCarcuss() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (1,1),"Rabbit");
        rabbit.die(world);
        program.simulate();
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        wolf.setHunger(50);
        double expectedEnergy = 200;
        program.simulate();

        assertEquals(expectedEnergy, wolf.getEnergy());
    }

    @Test
    void testDayBehaviorExpectsWolfToEatCarcussWhereEveryoneInHuntingPackGetsEnergy() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (1,1),"Rabbit");
        rabbit.die(world);
        program.simulate();
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,1), "Wolf", wolf.getPack());
        wolf.setHunger(50);
        wolf2.setHunger(50);
        program.simulate();
        program.simulate();

        System.out.println(world.getEntities());

        double expectedHungerWolf1 = Math.min(100, wolf.getHunger()+(0.5* rabbit.getEnergy()));
        double expectedHungerWolf2 = Math.min(100, wolf2.getHunger()+(0.5* rabbit.getEnergy()));



        assertEquals(expectedHungerWolf1, wolf.getHunger());
        assertEquals(expectedHungerWolf2, wolf2.getHunger());
    }

    @Test
    void testDayBehaviorExpectsWolfToAttackAndRabbitToDie() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (3,3),"Rabbit");
        rabbit.setEnergy(100);
        rabbit.skipTurn();
        program.simulate();
        rabbit.skipTurn();
        program.simulate();
        rabbit.skipTurn();
        program.simulate();
        assertEquals(rabbit.getMaxHealth()-wolf.getStrength(), rabbit.getHealth());
        assertEquals(world.getTile(new Location(3,3)).getClass(), Carcass.class);
    }

    @Test
    void testWolfEatingInAPack(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf", wolf.getPack(), 3, false);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (1,1),"Rabbit");
        rabbit.skipTurn();
        program.simulate();
        rabbit.skipTurn();
        program.simulate();
        program.simulate();

        int expectedHunger = Math.min(100,rabbit.getEnergy()+wolf.getEnergy());
        assertEquals(expectedHunger,wolf.getHunger());
        assertEquals(expectedHunger,wolf2.getHunger());

    }

    @Test
    void testWolfEatingAlone(){
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location (1,1),"Rabbit");
        rabbit.skipTurn();
        program.simulate();
        rabbit.skipTurn();
        program.simulate();

        int expectedHunger = Math.min(100,rabbit.getEnergy()+wolf.getEnergy());
        assertEquals(expectedHunger,wolf.getHunger());

    }

    @Test
    void testNightBehaviorWhereWolfIsInsideOfDenExpectsWolfToSleep() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        wolf.setHunger(100);
        wolf.setEnergy(90);
        world.setNight();
        program.simulate();
        program.simulate();

        assertEquals(100,wolf.getEnergy());
    }

    @Test
    void testThatWolfsIsRemovedFromPackWhenDead() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        wolf.die(world);
        program.simulate();

        assertEquals(0, wolf.getPack().getMembers().size());
    }

    @Test
    void testThatWolfsIsRemovedFromHuntingPackWhenDead() {
        Wolf wolf1 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
        Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf", wolf1.getPack(), 3, false);
        ObjectFactory.generateOnMap(world, new Location (3,3),"Rabbit");
        program.simulate();
        program.simulate();
        int expectedMembers = wolf2.getHuntingPack().getMembers().size()-1;
        wolf1.die(world);
        assertEquals(expectedMembers, wolf2.getHuntingPack().getMembers().size());
    }

    @Test
    void testThatWolvesInAnWolfpackWith3OrMoreMemebersAttackBear(){
    //rykket til K2_8a
    }
}
