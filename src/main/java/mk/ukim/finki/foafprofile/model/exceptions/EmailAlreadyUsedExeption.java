package mk.ukim.finki.foafprofile.model.exceptions;

public class EmailAlreadyUsedExeption extends RuntimeException{
    public EmailAlreadyUsedExeption() {
         super("There is already an user with this email.");
    }
}
