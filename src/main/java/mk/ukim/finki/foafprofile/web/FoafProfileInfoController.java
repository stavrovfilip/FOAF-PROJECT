package mk.ukim.finki.foafprofile.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.FoafProfileInfo;
import mk.ukim.finki.foafprofile.model.Friend;
import mk.ukim.finki.foafprofile.model.Picture;
import mk.ukim.finki.foafprofile.model.dto.FriendDto;
import mk.ukim.finki.foafprofile.service.FoafProfileInfoService;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import mk.ukim.finki.foafprofile.service.FriendService;
import mk.ukim.finki.foafprofile.service.PictureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@AllArgsConstructor
public class FoafProfileInfoController {

    private final FoafProfileInfoService foafProfileInfoService;
    private final FoafProfileService foafProfileService;
    private final FriendService friendService;
    private final PictureService pictureService;

    @GetMapping("/foafprofile/create")
    public String getFoafProfileCreatePage(Model model) {

        model.addAttribute("bodyContent", "createFoafPofile");

        return "master-template";
    }

    @PostMapping("/create")
    public String createFoafProfile(
            @RequestParam("title") String title,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("nickName") String nickName,
            @RequestParam("email") String email,
            @RequestParam("homepage") String homepage,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("workHomepage") String workHomepage,
            @RequestParam("workDescription") String workDescription,
            @RequestParam("schoolHomepage") String schoolHomepage,
            @RequestParam("friendFirstName1") String friendFirstName,
            @RequestParam("friendLastName1") String friendLastName1,
            @RequestParam("friendEmail1") String friendEmail,
            @RequestParam("friendFoafUri1") String friendFoafUri


//            @RequestParam("friend2") FriendDto friend2,
//            @RequestParam("friend3") FriendDto friend3,
//            @RequestParam("friend4") FriendDto friend4,
//            @RequestParam("friend5") FriendDto friend5
    ) throws IOException {

        ArrayList<Friend> friends = new ArrayList<>();
        if (friendEmail != null) {
            FriendDto friendDto = new FriendDto(friendFirstName, friendLastName1, friendEmail, friendFoafUri);
            friends.add(this.friendService.saveFriend(friendDto));
        }
        //da sredime so dto-to
//        if (friend2.getFriendEmail() != null) {
//            friends.add(this.friendService.saveFriend(friend2));
//        }
//        if (friend3.getFriendEmail() != null) {
//            friends.add(this.friendService.saveFriend(friend3));
//        }
//        if (friend4.getFriendEmail() != null) {
//            friends.add(this.friendService.saveFriend(friend4));
//        }
//        if (friend5.getFriendEmail() != null) {
//            friends.add(this.friendService.saveFriend(friend5));
//        }

        Picture picture1 = null;
        if (!picture.isEmpty() || picture != null) {
            picture1 = pictureService.store(picture);
        }
        FoafProfileInfo foafProfileInfo = this.foafProfileInfoService.saveProfile(title, firstName, lastName, nickName, email, homepage, phoneNumber,
                picture1, workHomepage, workDescription, schoolHomepage, friends);
        return "redirect:/profile/" + foafProfileInfo.getUri().toString();

    }

    @GetMapping("/profile/{id}")
    public String showFoafProfile(@PathVariable String id, Model model) {

        FoafProfile foafProfile = this.foafProfileService.getFoafProfileByUri(id);
        String foafProfileString = foafProfile.getProfile();
        model.addAttribute("profile", foafProfileString);
        model.addAttribute("bodyContent", "myProfile");

        return "master-template";

    }

}
