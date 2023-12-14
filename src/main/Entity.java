package main;

public abstract class Entity implements Spawnable{
    /**
     * @return the class of the entity
     */
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }
}
