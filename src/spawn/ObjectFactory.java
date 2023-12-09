package spawn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.management.RuntimeErrorException;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import Main.Entity;
import Main.Helper;
import Main.Spawnable;

import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

public class ObjectFactory {
    private ObjectFactory() {}

    private static String[] requiresWorld = {"Bear"};

    public static Object generateOffMap(World world, String className, Object... constructorArgs){
        if (Helper.doesArrayContain(requiresWorld, className)) {
            constructorArgs = prependArray(constructorArgs, world);
        }

        Object object = generateHelper(className, constructorArgs);

        if(object instanceof Entity) world.add(object);

        return object;
    }

    public static Object generateOnMap(World world, String className, Object... constructorArgs) {
        if (Helper.doesArrayContain(requiresWorld, className)) {
            constructorArgs = prependArray(constructorArgs, world);
        }

        Object object = generateHelper(className, constructorArgs);

        if(object instanceof Entity) world.add(object);
        if (object instanceof Spawnable) place(world, object);

        return object;
    }

    public static Object generateOnMap(World world, Location location, String className, Object... constructorArgs) {
        if (Helper.doesArrayContain(requiresWorld, className)) {
            constructorArgs = prependArray(constructorArgs, world);
        }
        
        Object object = generateHelper(className, constructorArgs);

        if(object instanceof Entity) world.add(object);
        if (object instanceof Spawnable) place(world, object, location);
        
        return object;
    }

    private static Object generateHelper(String className, Object... constructorArgs) {
        className = "Main." + className;

        try {
            Class<?> objectClass = Class.forName(className);

            Class<?>[] parameters = new Class[constructorArgs.length];
            for(int i = 0; i < parameters.length; i++){
                parameters[i] = convertToPrimitiveTypeIfThereIsOne(constructorArgs[i].getClass());
            }

            Constructor<?> objectConstructor = objectClass.getConstructor(parameters);

            return objectConstructor.newInstance(constructorArgs);

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        } catch (NoSuchMethodException e) {
            System.out.println("No such constructor in: " + className);
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

    private static Object[] prependArray(Object[] array, Object object) {
        Object[] newArray = new Object[array.length + 1];
        newArray[0] = object;

        for(int i = 0; i < array.length; i++) {
            newArray[i + 1] = array[i];
        }

        return newArray;
    }

    private static Class<?> convertToPrimitiveTypeIfThereIsOne(Class<?> preConvertedClass){
        Map<Class<?>, Class<?>> classMap = new HashMap<>();
        classMap.put(Integer.class, int.class);
        classMap.put(Boolean.class, boolean.class);
        classMap.put(Long.class, long.class);
        classMap.put(Short.class, short.class);
        classMap.put(Byte.class, byte.class);
        classMap.put(Float.class, float.class);
        classMap.put(Double.class, double.class);
        classMap.put(Character.class, char.class);
        return classMap.getOrDefault(preConvertedClass,preConvertedClass);
    }
}
