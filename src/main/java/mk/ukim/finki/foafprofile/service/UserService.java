package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.User;
import mk.ukim.finki.foafprofile.model.enumeration.Role;

public interface UserService {

    User login(String username, String password);

    User register(String username, String email, String firstName, String lastName, String password, String repeatPassword);

    User findUserByEmail(String email);
}
