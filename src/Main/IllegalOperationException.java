package Main;

public class IllegalOperationException extends RuntimeException {
    public IllegalOperationException(String error) {
        super(error);
    }
}
