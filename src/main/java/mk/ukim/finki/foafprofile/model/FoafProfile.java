package mk.ukim.finki.foafprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FoafProfile {

    @Id
    private String uri;

    private String profile;

    private String profileFile;

    @OneToOne
    private FoafProfileInfo foafProfileInfo;

}
