package services;

public class ValveException extends RuntimeException {
    private final String message;

    public ValveException(String errorMessage) {
        this.message = errorMessage;
    }

    public ValveException(Exception message) {
        this.message = message.getMessage();
    }

    @Override
    public String toString() {
        return "ValveException{" +
                "e='" + message + '\'' +
                '}';
    }
}
