package mk.ukim.finki.foafprofile.model.exceptions;

public class MissingPersonalInfoException extends RuntimeException {
    public MissingPersonalInfoException(){ super("First name, last name and email are required fields!");}

}
