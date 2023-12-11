package error;

public class NoEmptyLocationException extends RuntimeException {
    public NoEmptyLocationException() {
        super("There is no empty location in the world");
    }
}
