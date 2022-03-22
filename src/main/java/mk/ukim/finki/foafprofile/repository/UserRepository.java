package mk.ukim.finki.foafprofile.repository;

import mk.ukim.finki.foafprofile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findUserByEmail(String email);

    User findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String encode);
}
