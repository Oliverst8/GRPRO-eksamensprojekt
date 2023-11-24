package Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class ObjectFactory {


    private ObjectFactory(){

    }

    public static Object generate(World world, String className, Object... constructorArgs) {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Actor) {
            place(world, object);
        }

        return object;
    }

    public static Object generate(World world, Location location, String className, Object... constructorArgs) {

        Object object = generateHelper(className, constructorArgs);

        if (object instanceof Entity) {
            place(world, object, location);
        }
        
        return object;
    }

    // Writen together with ChatGPT
    private static Object generateHelper(String className, Object... constructorArgs) {
        className = className.toLowerCase();
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = "Main." + className;

        try {
            // Finds the type of class based on the className string
            Class<?> clazz = Class.forName(className);

            // Get the constructor with the specified parameter types
            Class<?>[] parameterTypes = new Class[constructorArgs.length];
            for (int i = 0; i < constructorArgs.length; i++) {
                parameterTypes[i] = constructorArgs[i].getClass();
            }

            //Find the constructor that has the matching parameters found earlier
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);

            // Creates the an instance of the object, using the constructer arguments and returns it
            return constructor.newInstance(constructorArgs);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        } catch (NoSuchMethodException e) {
            System.out.println("No such constructor: " + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (IllegalAccessException e) {
            System.out.println("No access to constructor" + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (InvocationTargetException e) {
            System.out.println("Could not invoke constructor: " + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (InstantiationException e) {
            System.out.println("Could not instantiate class: " + className);
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        // Failsafe
        throw new RuntimeErrorException(null, "Could not generate object: " + className);
    }

    private static void place(World world, Object object) {
        


        Location location;

        if(object instanceof NonBlocking) location = Helper.findNonBlockingEmptyLocation(world);
        else location = Helper.findEmptyLocation(world);


        world.setTile(location, object);
    }

    private static void place(World world, Object object, Location location) {
        world.setTile(location, object);
    }
}
