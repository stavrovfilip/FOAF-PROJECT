package mk.ukim.finki.foafprofile.model.exceptions;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {
        super("Passwords do not match. Try again!");
    }
}
