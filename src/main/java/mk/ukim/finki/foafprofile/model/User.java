package mk.ukim.finki.foafprofile.model;

import lombok.*;
import mk.ukim.finki.foafprofile.model.enumeration.Role;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Site_users")
public class User {

    @Id
    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    //mapped by = uri moze ke treba
    @OneToOne
    private FoafProfile myProfile;

    public User(String username, String password, String email, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

}
