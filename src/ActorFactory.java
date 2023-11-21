import itumulator.simulator.Actor;
import itumulator.world.World;

public class ActorFactory {
    private Actor actor;
    private int amount;

    public ActorFactory(World world, String actor, String amount) {
        switch(actor) {
            case "grass":
                this.actor = new Grass();
                break;
            case "burrow":
                this.actor = new Burrow();
                break;
            case "rabbit":
                this.actor = new Bunny();
                break;
        }

        if (amount.contains("-")) {
            String[] range = amount.split("-");

            int low = Integer.parseInt(range[0]);
            int high = Integer.parseInt(range[1]);

            this.amount = (int) (Math.random() * (high - low)) + low;
        } else {
            this.amount = Integer.parseInt(amount);
        }
    }

    private void place(World world) {
        for (int i = 0; i < amount; i++) {
            world.place(actor);
        }
    }
}
