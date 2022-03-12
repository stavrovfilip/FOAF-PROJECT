package mk.ukim.finki.foafprofile.repository;

import mk.ukim.finki.foafprofile.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Optional<Picture> findPictureByName(String name);
}
