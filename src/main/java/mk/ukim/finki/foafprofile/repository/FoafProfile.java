package mk.ukim.finki.foafprofile.repository;

import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoafProfile extends JpaRepository<FoafProfile, String> {
}
