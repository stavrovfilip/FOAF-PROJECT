package mk.ukim.finki.foafprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoafProfileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uri;
    private String title;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private String homepage;
    private String phoneNumber;
    @OneToOne
    private Picture picture;

    private String workHomepage;
    private String workDescription;

    private String schoolHomepage;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Friend> myFriends;

    public FoafProfileInfo(String uri, String title, String firstName, String lastName, String nickName, String email, String homepage, String phoneNumber, Picture picture, String workHomepage, String workDescription, String schoolHomepage, List<Friend> myFriends) {
        this.uri = uri;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.email = email;
        this.homepage = homepage;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.workHomepage = workHomepage;
        this.workDescription = workDescription;
        this.schoolHomepage = schoolHomepage;
        this.myFriends = myFriends;
    }
}
