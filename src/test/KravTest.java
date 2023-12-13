package test;

import java.util.*;

import main.*;

import spawn.Input;
import spawn.ObjectFactory;
import spawn.SpawningObject;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class KravTest {
    Program program;
    World world;

    @BeforeEach
    void setUp() {
        int size = 5; // Size of the world
        int delay = 1; // Delay between each turn (in ms)
        int display_size = 800; // Size of the display

        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }

    /**
     * Græs kan blive plantet når input filerne beskriver dette. Græs skal blot tilfældigt placeres.
     */
    @Test
    void K1_1a() {
        World world = generateWithInput(new Input("data/demo/d4.txt"));

        boolean containsGrass = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Grass.class)) {
                containsGrass = true;
                break;
            }
        }

        assertTrue(containsGrass);
    }

    /**
     * Græs kan nedbryde og forsvinde
     *
     * setEnergy 10 so it low
     * setNight so grass doesnt make photosynthesis
     * Call program.simulate twice so it calls decay twice and removes 5 energy times two
     */
    @Test
    void K1_1b() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "Grass");
        grass.setEnergy(10);
        world.setNight();
        program.simulate();
        program.simulate();

        assertFalse(world.contains(grass));
    }

    /**
     * Græs kan sprede sig
     *
     * Creates grass, sets its energy to 75 which is required in its dayBehaviour to spread
     */
    @Test
    void K1_1c() {
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, "Grass");
        grass.setEnergy(75);
        program.simulate();
        assertEquals(2,world.getEntities().size());
    }

    /**
     * Dyr kan stå på græs uden der sker noget med græsset.
     *
     * Create both object at the same place
     * Set rabbit to skip turn so it doesnt eat the grass.
     * assert true that their locations equal eachother
     */
    @Test
    void K1_1d() {
        Location location = new Location(0,0);
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, location,"Grass");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");

        rabbit.skipTurn();
        program.simulate();

        assertEquals(world.getLocation(grass),world.getLocation(rabbit));
    }

    /**
     * Kaniner kan placeres på kortet når input filerne beskriver dette. Kaniner skal blot tilfældigt placeres.
     */
    @Test
    void K1_2a() {
        World world = generateWithInput(new Input("data/demo/d5.txt"));

        boolean containsRabbit = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Rabbit.class)) {
                containsRabbit = true;
                break;
            }
        }

        assertTrue(containsRabbit);
    }

    /**
     * Kaniner kan dø, hvilket resulterer I at de fjernes fra verdenen.
     *
     * Spawn rabbit on world
     * set energy and hunger to 0 so it call die in daybehaviour
     * Assert that the object doesnt exist within the world
     */
    @Test
    void K1_2b() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world,"Rabbit");
        rabbit.setHunger(0);
        rabbit.setEnergy(0);
        program.simulate();
        assertFalse(world.contains(rabbit));
    }

    /**
     * Kaniner lever af græs som de spiser i løbet af dagen, uden mad dør en kanin.
     *
     * Create rabbit and grass. Observe if rabbit eats grass by comparing hunger
     * while(world doesnt contain grass and world contains rabbit) program.simulate
     * Assert that rabbit doesnt exist within the world.
     */
    @Test
    void K1_2c() {
        Location location = new Location(0,0);
        Grass grass = (Grass) ObjectFactory.generateOnMap(world, location,"Grass");
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");
        double hungerBeforeGrass = rabbit.getHunger();
        program.simulate();
        if(hungerBeforeGrass<rabbit.getHunger()){
            while(!world.contains(grass) && world.contains(rabbit)){
                program.simulate();
            }
        }
        assertFalse(world.contains(rabbit));
    }

    /**
     * Kaniners alder bestemmer hvor meget energi de har.
     * Creates rabbit
     * Gets the energy from when it was age 0
     * When it gets older than adultage the energy begins to fall
     * For loop ages rabbit and by the end of the loop adds 100 energy
     * Asserts that the energy at age 0 (100) is higher than rabbit.getenergy after even tho it gets 100 energy added
     */
    @Test
    void K1_2d() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location,"Rabbit");

        int energyBefore = rabbit.getEnergy(); //100

        for(int i = 0; i<rabbit.getAdultAge()+1 ; i++){
        world.setNight();
        while(world.isNight()){
            program.simulate();
        }
        program.simulate();
        rabbit.addEnergy(100);
        }
        System.out.println(rabbit.getAge());
        assertTrue(energyBefore>rabbit.getEnergy());
    }

    /**
     * Kaniner kan reproducere.
     *
     * Generates two rabbits. Gives them enough energy to call reproduce
     * Expects that burrow.getMembers.size gets larger by 1
     */
    @Test
    void K1_2e() {
        Burrow burrow = new Burrow(world, new Location(0,0));
        Rabbit rabbitInsideBurrow = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit", 3, burrow, true);
        rabbitInsideBurrow.setEnergy(100);
        rabbit2.setEnergy(100);
        int expected = burrow.getMembers().size()+1;
        program.simulate();

        assertEquals(expected,burrow.getMembers().size());
    }

    /**
     * Kaniner kan grave huller
     *
     */
    @Test
    void K1_2f_0(){
        Location rabbit1Location = new Location(0,0);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit");
        rabbit1.setHunger(100);
        rabbit1.setEnergy(100);
        program.simulate();

        assertNotNull(rabbit1.getNest());

    }

    /**
     * Kaniner kan dele eksisterende huller med andre kaniner.
     *
     */
    @Test
    void K1_2f_1(){
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit",3,burrow,true);
        Location rabbit2Location = new Location(2,2);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit2Location, "Rabbit");
        rabbit2.setHunger(100);
        rabbit2.setEnergy(25); //cant dig burrow
        while(!rabbit2.isInNest()){
            rabbit1.skipTurn();
            program.simulate();
        }
        assertEquals(rabbit1.getNest(),rabbit2.getNest());

    }

    /**
     * Kaniner kan kun være knyttet til et hul.little drop of white pixels
     *
     */
    @Test
    void K1_2f_2(){
        Location rabbit1Location = new Location(0,0);
        Location rabbit2Location = new Location(3,3);

        Burrow burrow1 = new Burrow(world, rabbit1Location);
        Burrow burrow2 = new Burrow(world, rabbit2Location);

        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit2Location, "Rabbit",3,burrow1,false);
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow2,false);

        rabbit1.setEnergy(25);
        rabbit2.setEnergy(25);

        world.setNight();
        program.simulate();
        program.simulate();

        assertFalse(rabbit1.isInNest());
        assertFalse(rabbit2.isInNest());
    }

    /**
     * Kaniner søger mod deres huller når det bliver aften, hvor de sover.
     * Burrow har koordinat 2,2
     * Kanin har 0,0
     * Det bliver nat og den søger hen imod
     * Kanin bør derfor nu have lokationen 1,1
     */
    @Test
    void K1_2g() {
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, new Location(2,2));
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow,false);
        world.setNight();
        program.simulate();
        Location forventetLocation = new Location(1,1);
        assertEquals(forventetLocation,world.getLocation(rabbit1));
    }

    /**
     * Huller kan enten blive indsat når input filerne beskriver dette, eller graves af kaniner. Huller skal blot blive tilfældigt placeret når de indgår i en input fil.
     */
    @Test
    void K1_3a() {
        World world = generateWithInput(new Input("data/demo/d8.txt"));

        boolean containsHole = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Hole.class)) {
                containsHole = true;
                break;
            }
        }

        assertTrue(containsHole);
    }

    /**
     * Dyr kan stå på et kaninhul uden der sker noget.
     * Spawnlocation for både burrow(med tilhørende hul) og rabbit er den samme
     * Vi simulerer programmet og verificerer at de begge stadig har samme lokation
     */
    @Test
    void K1_3b() {
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,rabbit1Location, "Rabbit",3,burrow,false);
        rabbit1.setEnergy(100);
        rabbit1.setHunger(99);

        Set<Location> possibleLocations = new HashSet<>(world.getEmptySurroundingTiles(world.getLocation(burrow.getEntries().iterator().next())));
        possibleLocations.add(rabbit1Location);

        program.simulate();

        assertTrue(possibleLocations.contains(world.getLocation(rabbit1)));
    }


    /**
     * Huller består altid minimum af en indgang.
     */
    @Test
    void KF1_1_0(){
        Location rabbit1Location = new Location(0,0);
        Burrow burrow = new Burrow(world, rabbit1Location);
        assertEquals(burrow.getEntries().size(),1);
    }

    /**
     * Huller kan dog være flere indgange som sammen former én kanin tunnel.
     */
    @Test
    void KF1_1_1(){
        Location rabbit1Location = new Location(0,0);

        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOffMap(world, "Rabbit",3,burrow,true);

        rabbit1.setEnergy(100);
        rabbit1.setHunger(100);

        program.simulate();
        program.simulate();

        assertEquals(burrow.getEntries().size(),2);
    }

    /**
     * Kaniner kan kun grave nye udgange mens de er i deres huller.
     */
    @Test
    void KF1_1_2(){
        Location rabbit1Location = new Location(0,0);

        Burrow burrow = new Burrow(world, rabbit1Location);
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world, rabbit1Location,"Rabbit",3,burrow,false);

        rabbit1.setEnergy(100);
        rabbit1.setHunger(99); // nok til at grave, ikke nok til at gå ind i burrow

        program.simulate();
        program.simulate(); //et par simulations

        assertEquals(burrow.getEntries().size(),1);
    }

    /**
     * Ulve kan placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K2_1a() {
        World world = generateWithInput(new Input("data/demo/d2.txt"));

        boolean containsWolf = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Wolf.class)) {
                containsWolf = true;
                break;
            }
        }

        assertTrue(containsWolf);
    }

    /**
     * Ulve kan dø, hvilket resulterer I at de fjernes fra verdenen.
     * Spawn ulv, remove health, simulate og ulven dør
     * Bliver lavet om til en carcass, hvis den gør det så sættes indeholdercarcass til 1
     * assert that world.getentities.size == indeholdercarcass
     */
    @Test
    void K2_1b() {
        Location location = new Location(0,0);

        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, location, "Wolf");

        wolf.setHealth(100);
        wolf.setEnergy(100);
        wolf.removeHealth(100,world);

        program.simulate();

        assertSame(world.getTile(location).getClass(), Carcass.class);
    }

    /**
     * Ulve jager andre dyr og spiser dem for at opnå energi
     * If wolf movestowards rabbit location log hunger before
     * Simulate three times
     * Asserttrue that hungerbefore is lower than hunger after
     */
    @Test
    void K2_1c() {
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(4,4), "Wolf");
        ObjectFactory.generateOnMap(world,new Location(0,0), "Rabbit");
        double hungerBeforeConsuming = wolf.getHunger();

        program.simulate(); //4,4 - 3,3
        program.simulate(); //3,3 - 2,2
        program.simulate(); //2,2 - 1,1
        program.simulate(); // kill
        program.simulate(); //eat

        assertTrue(hungerBeforeConsuming<wolf.getHunger());
    }

    /**
     * Ulve er et flokdyr. De søger konstant mod andre ulve i flokken, og derigennem ’jager’ sammen.
     */
    @Test
    void K2_2a_0(){
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

    /**
     * Når inputfilen beskriver (på en enkelt linje) at der skal placeres flereulve, bør disse automatisk være i samme flok.
     */
    @Test
    void K2_2a_1(){
        World world = generateWithInput(new Input("data/demo/d9.txt"));

        Map<Object, Location> entities = world.getEntities();
        List<Wolf> wolfList = new ArrayList<>();
        int packsize = 0;

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Wolf.class)) {
                wolfList.add((Wolf) entity);
                packsize = ((Wolf) entity).getPack().getMembers().size();
            }
        }
        assertEquals(packsize,world.getEntities().size());
    }


    /**
     * Ulve og deres flok, tilhører en ulvehule, det er også her de formerer sig.
     * Ulve ’bygger’ selv deres huler. Ulve kan ikke lide andre ulveflokke og deres huler.
     * De prøver således at undgå andre grupper. Møder en ulv en ulv fra en anden flok, kæmper de mod hinanden.
     */
    @Test
    void K2_3a() {
        int wolf3HealthBefore = 200;
        int wolf3HealthAfter = 200;


        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf",5);
        wolf.setHunger(100);

        program.simulate();

        if(wolf.isInNest()){
            wolf.setHunger(50);
            Wolf wolf2 = null;
            program.simulate();

            if(!wolf.isInNest()) {
                wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,0), "Wolf", wolf.getPack(), 5, false);
                wolf.setHunger(100);
                wolf2.setHunger(100);

                program.simulate();
                program.simulate();
                program.simulate();

            }

            if(world.getEntities().size()>=4) {

                Wolf wolf3 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf", 5);
                wolf3.setHealth(200);
                wolf3HealthBefore = wolf3.getHealth();

                program.simulate();
                program.simulate();

                if(!Objects.equals(world.getLocation(wolf3), new Location(0, 0))){
                    wolf.setEnergy(20);
                    wolf2.setEnergy(20);
                    wolf.setHunger(50);
                    wolf2.setHunger(50);
                    wolf3.skipTurn();
                    program.simulate();
                    wolf3.skipTurn();
                    program.simulate();
                    wolf3.skipTurn();
                    program.simulate();
                    wolf3HealthAfter = wolf3.getHealth();
                }
            }
        }
        assertTrue(wolf3HealthBefore>wolf3HealthAfter);
    }

    /**
     * Kaniner frygter ulve og forsøger så vidt muligt at løbe fra dem.
     */
    @Test
    void K2_4a() {
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,new Location(1,1), "Rabbit");
        Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,0), "Wolf");
        Location assumedRabbitLocation = new Location(2,2);
        wolf.skipTurn();
        program.simulate();

        assertEquals(assumedRabbitLocation,world.getLocation(rabbit1));
    }

    /**
     * Bjørne kan placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K2_5a() {
        World world = generateWithInput(new Input("data/demo/d3.txt"));

        boolean containsBear = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Bear.class)) {
                containsBear = true;
                break;
            }
        }

        assertTrue(containsBear);
    }

    /**
     * Bjørne jager ligesom ulve, og spiser også alt.
     */
    @Test
    void K2_5b() {
        ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf");
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        program.simulate();
        assertEquals(new Location(1,1),world.getLocation(bear));
    }

    /**
     * Kaniner frygter bjørne og forsøger så vidt muligt at løbe fra dem.
     */
    @Test
    void K2_5c() {
        Rabbit rabbit1 = (Rabbit) ObjectFactory.generateOnMap(world,new Location(1,1), "Rabbit");
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        Location assumedRabbitLocation = new Location(2,2);
        bear.skipTurn();
        program.simulate();

        assertEquals(assumedRabbitLocation,world.getLocation(rabbit1));
    }

    /**
     * Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den ’bor’.
     * Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra.
     * Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet
     */
    @Test
    void K2_6a() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear",new Location(0,0));
        assertEquals(bear.getTerritory(),new Location(0,0));
    }

    /** 
     * Dertil spiser bjørne også bær fra buske (såsom blåbær og hindbær) når de gror i området.
     * Bær er en god ekstra form for næring for bjørnen (om end det ikke giver samme mængde energi som når de spiser kød)
     * men som det er med buske går der tid før bær gror tilbage.
     * Bær skal indsættes på kortet når inputfilerne beskriver dette.
     */
    @Test
    void K2_7a() {
        boolean bearHasEaten = false;
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(2,1), "Bear");
        Berry berry = (Berry) ObjectFactory.generateOnMap(world, "Berry");

        double before = 0;

        while(bear.getAge()==0){
            program.simulate();
        }


        for(int i = 0; i<3;i++) {
            program.simulate();
        }



        if(before < bear.getHunger()) {
            bearHasEaten = true;
        }

        assertTrue(bearHasEaten);
    }

    /**
     * Bjørnen er naturligvis vores øverste rovdyr i denne lille fødekæde, men det
     * hænder at en stor nok gruppe ulve kan angribe (og dræbe) en bjørn.
     * Dette vil i praksis være hvis flere ulve af samme flok er i nærheden af en bjørn.
     */
    @Test
    void K2_8a() {
        Wolf wolf1 = (Wolf) ObjectFactory.generateOnMap(world, new Location(0,1), "Wolf", 5);
        ObjectFactory.generateOnMap(world, new Location(1,1), "Wolf", wolf1.getPack(), 3, false);
        ObjectFactory.generateOnMap(world, new Location(1,0), "Wolf", wolf1.getPack(), 3, false);
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(3,3), "Bear");

        int bearHealthBeforeAtt = bear.getHealth();
        bear.skipTurn();
        program.simulate();
        bear.skipTurn();
        program.simulate();
        bear.skipTurn();
        program.simulate();

        int bearHealthAfterAtt = bear.getHealth();
        assertTrue(bearHealthBeforeAtt>bearHealthAfterAtt);
    }

    /**
     * Hvis den ene ulv bliver voldsomt såret, underkaster den sig den sejrende ulvs flok. En såret ulv har brug for hvile før den kan fortsætte.
     */
    @Test
    void KF2_1() {
        throw new UnsupportedOperationException("Ikke lavet endnu");
    }

    /**
     * Dog er bjørnen ikke et flokdyr, og mødes kun med andre bjørne når de skal
     * parre sig. Bjørnen kan også dø og fjernes her fra verdenen.
     */
    @Test
    void KF2_2() {
        World world = generateWithInput(new Input("data/demo/d10.txt"));

        List<Object> entities = new ArrayList<>(world.getEntities().keySet());
        List<Bear> bearlist = new ArrayList<>();

        for(Object object : entities){
            if(object.getClass() == Bear.class){
                bearlist.add((Bear) object);
            }
        }

        while(!bearlist.get(0).isAdult()){
            for(Bear bear : bearlist){
                bear.setHunger(100);
                bear.addEnergy(bear.getMaxEnergy());
            }
            program.simulate();
        }
        while(entities.size()==world.getEntities().size()){
            program.simulate();
            System.out.println(world.getEntities());
        }
        assertTrue(entities.size()<world.getEntities().size());
    }

    /**
     * Bjørne der ikke er klar til at parre sig, angriber andre bjørne der bevæger sig ind
     * på deres områder. Tilsvarende angriber de andre dyr.
     */
    @Test
    void KF2_3() {
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        Bear bear2 = (Bear) ObjectFactory.generateOnMap(world, new Location(4,4), "Bear");
        double hungerBefore = bear.getHunger();
        while(world.contains(bear2)){
            bear.setHealth(bear.getMaxHealth());
            program.simulate();
        }
        program.simulate();
        assertTrue(hungerBefore<bear.getHunger());
    }

    /**
     * Opret ådsler, som placeres på kortet når input filerne beskriver dette.
     */
    @Test
    void K3_1a() {
        World world = generateWithInput(new Input("data/demo/d7.txt"));

        boolean containsCarcass = false;

        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Carcass.class)) {
                containsCarcass = true;
                break;
            }
        }

        assertTrue(containsCarcass);
    }

    /**
     * Når dyr dør nu, skal de efterlade et ådsel. Ådsler kan spises ligesom dyr kunne
     * tidligere, dog afhænger mængden af ’kød’ af hvor stort dyret der døde er. Således
     * spises dyr ikke direkte længere når det slås ihjel, i stedet spises ådslet. Alle dyr som er
     * kødædende spiser ådsler
     */
    @Test
    void K3_1b() {
        Location location = new Location(0,0);
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, location, "Rabbit");
        rabbit.removeHealth(100,world);

        double wolfRabbitHunger = 0;
        double wolfWolfHunger = 0;

        if(world.getTile(location).getClass().equals(Carcass.class)){
            Location wolfLocation = new Location(1,1);
            Wolf wolf = (Wolf) ObjectFactory.generateOnMap(world, wolfLocation, "Wolf");
            program.simulate();
            wolfRabbitHunger = wolf.getHunger();
            if(world.isTileEmpty(location)){
                wolf.removeHealth(wolf.getHealth(),world);
                Wolf wolf2 = (Wolf) ObjectFactory.generateOnMap(world, new Location(2,2), "Wolf");
                program.simulate();
                wolfWolfHunger = wolf2.getHunger();
            }
        }
        assertTrue(wolfRabbitHunger<wolfWolfHunger);
    }

    /**
     * Ådsler bliver dårligere med tiden og nedbrydes helt – selvom det ikke er spist
     * op (altså forsvinder det)! Det forsvinder naturligvis også hvis det hele er spist.
     */
    @Test
    void K3_1c() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        rabbit.setHealth(100);
        rabbit.removeHealth(100,world);

        program.simulate();

        Carcass carcass = (Carcass) world.getTile(new Location(0,0));

        for(int i = 0; i <= 100; i++){
            program.simulate();
        }

        assertFalse(world.contains(carcass));
    }

    /**
     * Udover at ådsler nedbrydes, så hjælper svampene til. Således kan der opstå
     * svampe I et ådsel.
     * Dette kan ikke ses på selve kortet, men svampen lever I selve ådslet.
     * Når ådslet er nedbrudt (og forsvinder), og hvis svampen er stor nok, kan den ses som
     * en svamp placeret på kortet, der hvor ådsle lå. For at læse inputfilerne skal du sikre
     * dig, at et ådsel kan indlæses med svamp.
     */
    @Test
    void K3_2a() {
        Boolean containsFungi = false;
        Carcass carcass = null;
        Bear bear = (Bear) ObjectFactory.generateOnMap(world, new Location(0,0), "Bear");
        bear.removeHealth(bear.getHealth(),world);

        System.out.println(world.getEntities());
        Map<Object, Location> entities = world.getEntities();

        for(Object entity : entities.keySet()) {
            if (entity.getClass().equals(Carcass.class)) {
                carcass = (Carcass) entity;
                break;
            }
        }
        while(!carcass.isInfected()){
            program.simulate();
        }
        while(world.contains(carcass)){
            program.simulate();
        }

        List<Object> entities2 = new ArrayList<>(world.getEntities().keySet());

        assertEquals(1, entities2.size());
        assertInstanceOf(Ghoul.class, entities2.get(0));
        assertInstanceOf(Ghoul.class, world.getTile(new Location(0,0)));

    }

    /**
     * Svampe kan kun overleve, hvis der er andre ådsler den kan sprede sig til i
     * nærheden. Er dette ikke tilfældet, vil svampen også dø efter lidt tid. Desto større ådslet
     * er, desto længere vil svampen leve efter ådslet er væk. Da svampen kan udsende
     * sporer, kan den række lidt længere end kun de omkringliggende pladser.
     */
    @Test
    void K3_2b() {
        Carcass carcass = (Carcass) ObjectFactory.generateOnMap(world, new Location(2,2), "Carcass");
        ObjectFactory.generateOnMap(world, new Location(1,1), "Ghoul");

        program.simulate();

        assertTrue(carcass.isInfected());
    }

    /**
     * Når en svamp dør, er jorden ekstra gunstig. Derfor opstår græs på sådanne
     * felter, når svampen dør.
     */
    @Test
    void KF3_1a() {
        throw new UnsupportedOperationException("Ikke lavet endnu");
    }

    /**
     * Ikke alle typer svampe lever på døde dyr. Der eksisterer også en anden type
     * svamp (Cordyceps).
     * Denne svamp spreder sig til, og styrer, (kun) levende dyr. Deres
     * livscyklus er den samme som de tidligere svampe; de nedbryder langsomt dyret, og er
     * der ikke mere tilbage af dyret, dør svampen snart efter. Når svampen har tæret nok på
     * dyret, dør dyret. Da denne svamp nedbryder dyret mens det lever, er der ikke noget
     * ådsel efter døden. Svampen dør også når dyret dør, med undtagelsen af krav
     */
    @Test
    void KF3_2a() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Cordyceps cordyceps = new Cordyceps();
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);
        if(rabbit.isInfected()){
            rabbit.setHealth(0);
            program.simulate();
        }

        assertFalse(world.contains(rabbit));
        assertFalse(world.contains(cordyceps));
    }

    /**
     * Når Cordyceps’ vært dør, forsøger den at sprede sig til levende dyr i nærheden,
     * og kun på dette tidspunkt. Igen kan denne svamp sprede sig lidt længere end de
     * omkringliggende pladser da den sender sporer ud.
     */
    @Test
    void KF3_3a() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        Rabbit rabbit2 = (Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit");
        Cordyceps cordyceps = new Cordyceps();
        world.add(cordyceps);

        rabbit.setInfected(cordyceps);
        rabbit.setHealth(0);
        program.simulate();


        assertFalse(world.contains(rabbit));
        assertEquals(cordyceps, rabbit2.getFungi());
    }

    /**
     * Når et dyr er inficeret med Cordyceps svampen, overtager svampen dyrets
     * handlinger. Dyret gør derfor som svampen bestemmer, hvilket er at søge mod andre
     * dyr af samme art.
     */
    @Test
    void KF3_3b() {
        Rabbit rabbit = (Rabbit) ObjectFactory.generateOnMap(world, new Location(0,0), "Rabbit");
        rabbit.setInfected(new Cordyceps());
        ((Rabbit) ObjectFactory.generateOnMap(world, new Location(2,2), "Rabbit")).skipTurn();
        ((Wolf) ObjectFactory.generateOnMap(world, new Location(1,2), "Wolf")).skipTurn();

        program.simulate();

        assertEquals(new Location(1,1), world.getLocation(rabbit));
    }




    /**
     * Useful methods that gets used
     */
    private static void generateObjects(World world, ArrayList<SpawningObject> objects) {
        for (SpawningObject object : objects) {
            Pack pack = null;

            // Create pack if spawning object is a wolf
            if (object.getClassName().equals("Wolf")) pack = new Pack();

            for (int i = 0; i < object.getAmount(); i++) {
                Object newObject = null;

                // Generate object
                if (object.getClassName().equals("Wolf")) {
                    newObject = ObjectFactory.generateOnMap(world, object.getClassName(), pack);
                } else if(object.getLocation() != null) {
                    newObject = ObjectFactory.generateOnMap(world, object.getLocation(), object.getClassName(), object.getLocation());
                } else {
                    newObject = ObjectFactory.generateOnMap(world, object.getClassName());
                }

                // Set infected
                if (object.isInfected()) {
                    if (newObject instanceof Carcass) {
                        ((Carcass) newObject).setInfected(new Ghoul());
                    } else {
                        ((Animal) newObject).setInfected(new Cordyceps());
                    }
                }
            }
        }
    }

    private World generateWithInput(Input input) {
        int delay = 250;
        int display_size = 1000;
        int size = input.getSize();

        program = new Program(size, display_size, delay);
        World world = program.getWorld();
        generateObjects(world, input.getObjects());

        return world;


    }
}
