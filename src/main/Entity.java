package main;

public abstract class Entity {
    /**
     * @return the class of the entity
     */
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }
}
