package mk.ukim.finki.foafprofile.repository;

import mk.ukim.finki.foafprofile.model.FoafProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoafProfileRepository extends JpaRepository<FoafProfile, String> {
}
