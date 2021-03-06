package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.model.Picture;

import java.util.List;
import java.util.Optional;

public interface FoafProfileInfoService {

    public FoafProfileInfo saveProfile(String title, String firstName, String lastName,
                                       String nickName, String email, String homepage, String phoneNumber, Picture picture,
                                       String workHomepage, String workDescription, String schoolHomepage, List<Friend> myFriends, String username);

    public FoafProfileInfo updateProfile(Long id, String title, String lastName,
                                         String nickName, String homepage, String phoneNumber, Picture picture,
                                         String workHomepage, String workDescription, String schoolHomepage, List<Friend> myFriends, String username);

    public FoafProfileInfo findById(Long id);

    public FoafProfileInfo findByURI(String uri);

    public FoafProfileInfo findByEmail(String email);

    public void deleteProfile(String uri, String username);

}
