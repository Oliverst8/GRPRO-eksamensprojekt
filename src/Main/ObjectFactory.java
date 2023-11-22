package Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class ObjectFactory {
    public static Object generate(World world, String className, Object... constructorArgs) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Load the class dynamically
        Class<?> clazz = Class.forName(className);

        // Get the constructor with the specified parameter types
        Class<?>[] parameterTypes = new Class[constructorArgs.length];
        for (int i = 0; i < constructorArgs.length; i++) {
            parameterTypes[i] = constructorArgs[i].getClass();
        }

        Constructor<?> constructor = clazz.getConstructor(parameterTypes);

        Object object = constructor.newInstance(constructorArgs);

        if (object instanceof Actor) {
            place(world, object);
        }

        // Create an instance using the specified constructor and arguments
        return constructor.newInstance(constructorArgs);
    }

    private static void place(World world, Object object) {
        Random r = new Random();

        Location location = new Location(r.nextInt(),r.nextInt());

        world.setTile(location, object);
    }

    private static void place(World world, Object object, Location location) {
        world.setTile(location, object);
    }
}
