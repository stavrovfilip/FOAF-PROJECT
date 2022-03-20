package mk.ukim.finki.foafprofile.repository;

import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoafProfileInfoRepository extends JpaRepository<FoafProfileInfo, Long> {

    FoafProfileInfo findByUri(String uri);

    FoafProfileInfo findByEmail(String email);

    FoafProfileInfo findByNickName(String nickname);

    FoafProfileInfo findByFirstNameAndLastName(String firstName, String lastName);
}
