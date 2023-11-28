package Main;

public abstract class InAnimate extends Entity {
    @Override
    protected String getPNGPath(){
        return getType();
    }
}
