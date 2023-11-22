package Main;

import java.lang.reflect.InvocationTargetException;

import itumulator.executable.Program;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {

        Input input = new Input("data/week1/t1-1a.txt");

        int size = input.getSize(); // størrelsen af vores 'map' (dette er altid kvadratisk)
        int delay = 100; // forsinkelsen mellem hver skridt af simulationen (i ms)
        int display_size = 800; // skærm opløsningen (i px)

        Program program = new Program(size, display_size, delay); // opret et nyt program
        World world = program.getWorld(); // hiv verdenen ud, som er der hvor vi skal tilføje ting!

        // Generates the objects in the world
        for (String object : input.getObjects().keySet()) {
            for (int i = 0; i < input.getObjects().get(object); i++) {
                try {
                    ObjectFactory.generate(world, object);
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (NoSuchMethodException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                } catch (InvocationTargetException e) {
                    System.out.println(e.getMessage());
                } catch (InstantiationException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        program.show(); // viser selve simulationen
        for (int i = 0; i < 200; i++) {
            program.simulate();
        } // kører 200 runder, altså kaldes 'act' 200 gange for alle placerede aktører
    }
}