package Main;

import itumulator.simulator.Actor;
import itumulator.world.World;

public interface Behavior extends Actor {

    @Override
    default void act(World world) {
        setDay(world.isDay());
        doesAge();
        if(isTurnSkipped()) {
            setSkipTurn(false);
            return;
        }

        if(isDay()) dayBehavior(world);
        else nightBehavior(world);

    }

    abstract void doesAge();

    abstract void setSkipTurn(boolean skipTurn);

    abstract boolean isTurnSkipped();

    abstract void skipTurn();

    abstract boolean isDay();

    abstract void setDay(boolean isDay);

    abstract void dayBehavior(World world);

    abstract void nightBehavior(World world);

}
