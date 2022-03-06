package mk.ukim.finki.foafprofile.model.exceptions;

public class FoafProfileInfoNotFoundException extends RuntimeException{
    public FoafProfileInfoNotFoundException() {
        super("Foaf profile info not found.");
    }
}
