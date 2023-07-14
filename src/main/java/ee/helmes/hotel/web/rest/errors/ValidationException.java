package ee.helmes.hotel.web.rest.errors;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super("serverMessage.error." + message);
    }
}
