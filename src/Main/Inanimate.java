package Main;

public abstract class Inanimate extends Entity implements Spawnable {
    @Override
    public String getPNGPath() {
        return getType();
    }
}
