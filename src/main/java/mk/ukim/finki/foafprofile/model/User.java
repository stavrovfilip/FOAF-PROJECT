package mk.ukim.finki.foafprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.foafprofile.model.enumeration.Role;


import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

}
