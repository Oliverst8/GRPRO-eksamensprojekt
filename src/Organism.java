import itumulator.simulator.Actor;
import itumulator.world.World;

abstract class Organism implements Actor {
    private int age;
    private int health;

    public Organism() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void grow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void die(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getAge() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getHealth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
