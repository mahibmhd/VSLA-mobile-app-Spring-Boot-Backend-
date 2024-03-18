package vsla.exceptions.customExceptions;

import org.springframework.security.core.AuthenticationException;

public class BannedUserException extends AuthenticationException {

    public BannedUserException(String message) {
        super(message);
    }
}
