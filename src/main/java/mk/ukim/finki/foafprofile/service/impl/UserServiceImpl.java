package mk.ukim.finki.foafprofile.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.User;
import mk.ukim.finki.foafprofile.model.enumeration.Role;
import mk.ukim.finki.foafprofile.model.exceptions.InvalidUserCredentialException;
import mk.ukim.finki.foafprofile.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.foafprofile.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.foafprofile.repository.UserRepository;
import mk.ukim.finki.foafprofile.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.InvalidModuleDescriptorException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Login existing user on platform
     *
     * @param username
     * @param password
     * @return User
     */
    @Override
    public User login(String username, String password) {
        return this.userRepository.findByUsernameAndPassword(username, passwordEncoder.encode(password)).orElseThrow(InvalidUserCredentialException::new);
    }

    /**
     * Registering new user on platform
     *
     * @param username
     * @param email
     * @param firstName
     * @param lastName
     * @param password
     * @param repeatPassword
     * @return User
     */
    @Override
    public User register(String username, String email, String firstName, String lastName, String password, String repeatPassword) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new InvalidUserCredentialException();
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            throw new InvalidUserCredentialException();
        }
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if (this.userRepository.findById(username).isPresent())
            throw new UsernameAlreadyExistsException();

        String newpass = passwordEncoder.encode(password);
        User user = new User(username, newpass, email, firstName, lastName, Role.ROLE_USER);
        return userRepository.save(user);
    }

    /**
     * Returning user associated with provided email
     *
     * @param email
     * @return User
     */
    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    /**
     * Listing all users stored in database
     *
     * @return List<User>
     */
    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    /**
     * Returning FOAF profile associated with user with provided username
     *
     * @param username
     * @return FoafProfile
     */
    @Override
    public FoafProfile findFoafProfileByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        return user.getMyProfile();

    }

    @Override
    public User findUserByFoafProfile(FoafProfile foafProfile) {

        return this.userRepository.findByMyProfile(foafProfile);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(InvalidModuleDescriptorException::new);
        UserDetails user1 = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList()));
        return user1;
    }
}
