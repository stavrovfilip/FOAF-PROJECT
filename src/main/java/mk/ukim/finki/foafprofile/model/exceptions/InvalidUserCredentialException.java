package mk.ukim.finki.foafprofile.model.exceptions;

public class InvalidUserCredentialException extends RuntimeException{
    public InvalidUserCredentialException() {
        super("You need to fill out all required boxes!!!");
    }
}
