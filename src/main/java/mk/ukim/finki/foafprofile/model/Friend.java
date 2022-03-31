package mk.ukim.finki.foafprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "friend_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String foafProfileUri;
    @ManyToOne
    private FoafProfileInfo foafProfileInfo;

    public Friend(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
