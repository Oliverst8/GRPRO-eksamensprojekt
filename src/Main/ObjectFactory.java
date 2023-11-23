package Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class ObjectFactory {
    public static Object generate(World world, String className, Object... constructorArgs) {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Actor) {
            place(world, object);
        }

        return object;
    }

    public static Object generate(World world, Location location, String className, Object... constructorArgs) {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Actor) {
            place(world, object, location);
        }
        
        return object;
    }

    private static Object generateHelper(String className, Object... constructorArgs) {
        className = className.toLowerCase();
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = "Main." + className;
        
        Constructor<?> constructor;

        try {
            // Load the class dynamically
            Class<?> clazz = Class.forName(className);

            // Get the constructor with the specified parameter types
            Class<?>[] parameterTypes = new Class[constructorArgs.length];
            for (int i = 0; i < constructorArgs.length; i++) {
                parameterTypes[i] = constructorArgs[i].getClass();
            }

            constructor = clazz.getConstructor(parameterTypes);

            // Create an instance using the specified constructor and arguments
            return constructor.newInstance(constructorArgs);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        } catch (NoSuchMethodException e) {
            System.out.println("No such constructor: " + className);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (IllegalAccessException e) {
            System.out.println("No access to constructor" + className);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (InvocationTargetException e) {
            System.out.println("Could not invoke constructor: " + className);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (InstantiationException e) {
            System.out.println("Could not instantiate class: " + className);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        // If we get here, we didn't find the class in the Main package
        return null;
    }

    private static void place(World world, Object object) {
        
        Random r = new Random();

        Location location;

        do{    
            location = new Location(r.nextInt(world.getSize()),r.nextInt(world.getSize()));
        } while (!world.isTileEmpty(location));    


        world.setTile(location, object);
    }

    private static void place(World world, Object object, Location location) {
        world.setTile(location, object);
    }
}
