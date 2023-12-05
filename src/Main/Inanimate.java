package Main;

public abstract class Inanimate extends Entity {
    @Override
    protected String getPNGPath() {
        return getType();
    }
}
