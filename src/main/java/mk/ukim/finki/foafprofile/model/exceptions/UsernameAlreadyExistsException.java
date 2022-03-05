package mk.ukim.finki.foafprofile.model.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException() {
        super("This username is already taken. Try another one!");
    }
}
