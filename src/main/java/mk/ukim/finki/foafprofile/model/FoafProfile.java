package mk.ukim.finki.foafprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FoafProfile {

    @Id
    private String uri;

    @Column(length = 1024)
    private String profile;

    private String profileFile;

    @OneToOne(cascade = CascadeType.ALL)
    private FoafProfileInfo foafProfileInfo;

}
