package mk.ukim.finki.foafprofile.repository;

import mk.ukim.finki.foafprofile.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {
}
