package mk.ukim.finki.foafprofile.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.*;
import mk.ukim.finki.foafprofile.model.dto.FriendDto;
import mk.ukim.finki.foafprofile.service.FoafProfileInfoService;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import mk.ukim.finki.foafprofile.service.FriendService;
import mk.ukim.finki.foafprofile.service.PictureService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
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

    @GetMapping("/foafprofile/edit/{id}")
    public String editFoafProfilePage(@PathVariable Long id, Model model) {
        if (this.foafProfileInfoService.findById(id).isPresent()) {
            FoafProfileInfo foafProfileInfo = this.foafProfileInfoService.findById(id).get();
            model.addAttribute("foafProfileInfo", foafProfileInfo);
            model.addAttribute("bodyContent", "createFoafPofile");
            return "master-template";
        }
        return "redirect:/profiles?error=FoafProfileNotFound";
    }


    @PostMapping("/foafprofile/create")
    public String createFoafProfile(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam("email") String email,
            @RequestParam(value = "homepage", required = false) String homepage,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam(value = "workHomepage", required = false) String workHomepage,
            @RequestParam(value = "workDescription", required = false) String workDescription,
            @RequestParam(value = "schoolHomepage", required = false) String schoolHomepage,
            @RequestParam(value = "friendFirstName", required = false) String friendFirstName,
            @RequestParam(value = "friendLastName", required = false) String friendLastName1,
            @RequestParam(value = "friendEmail", required = false) String friendEmail,
            @RequestParam(value = "friendFoafUri", required = false) String friendFoafUri,
            @RequestParam(value = "friendFirstName2", required = false) String friendFirstName2,
            @RequestParam(value = "friendLastName2", required = false) String friendLastName2,
            @RequestParam(value = "friendEmail2", required = false) String friendEmail2,
            @RequestParam(value = "friendFoafUri2", required = false) String friendFoafUri2,
            @RequestParam(value = "friendFirstName3", required = false) String friendFirstName3,
            @RequestParam(value = "friendLastName3", required = false) String friendLastName3,
            @RequestParam(value = "friendEmail3", required = false) String friendEmail3,
            @RequestParam(value = "friendFoafUri3", required = false) String friendFoafUri3,
            HttpServletRequest req
    ) throws IOException {

        String username = req.getRemoteUser();
        ArrayList<Friend> friends = new ArrayList<>();

        if (friendEmail != null) {
            if (!friendEmail.isEmpty() || !friendFirstName.isEmpty() || !friendLastName1.isEmpty()) {
                FriendDto friendDto = new FriendDto(friendFirstName, friendLastName1, friendEmail, friendFoafUri);
                friends.add(this.friendService.saveFriend(friendDto));
            }
        }

        if (friendEmail2 != null) {
            if (!friendEmail2.isEmpty() || !friendFirstName2.isEmpty() || !friendLastName2.isEmpty()) {
                friends.add(this.friendService.saveFriend(new FriendDto(friendFirstName2, friendLastName2, friendEmail2, friendFoafUri2)));
            }
        }

        if (friendEmail3 != null) {
            if (!friendEmail3.isEmpty() || !friendFirstName3.isEmpty() || !friendLastName3.isEmpty()) {
                friends.add(this.friendService.saveFriend(new FriendDto(friendFirstName3, friendLastName3, friendEmail3, friendFoafUri3)));
            }
        }

        Picture picture1 = null;
        if (!picture.isEmpty() || picture != null) {
            picture1 = pictureService.store(picture);
        }

        FoafProfileInfo foafProfileInfo = null;
        if (id != null) {
            foafProfileInfo = this.foafProfileInfoService.updateProfile(id, title, lastName, nickName, homepage,
                    phoneNumber, picture1, workHomepage, workDescription, schoolHomepage, friends, username);
        } else {
            foafProfileInfo = this.foafProfileInfoService.saveProfile(title, firstName, lastName, nickName, email, homepage, phoneNumber,
                    picture1, workHomepage, workDescription, schoolHomepage, friends,username);
        }


        return "redirect:/profile/" + foafProfileInfo.getUri().toString();

    }

    @GetMapping("/profile/{id}")
    public String showFoafProfile(@PathVariable String id, Model model) {

        FoafProfile foafProfile = this.foafProfileService.getFoafProfileByUri(id);
        String foafProfileString = foafProfile.getProfile();
        model.addAttribute("profile", foafProfileString);
        model.addAttribute("id", foafProfile.getUri());
        model.addAttribute("bodyContent", "myProfile");

        return "master-template";

    }

    @RequestMapping(value = "/foafprofile/download", method = RequestMethod.GET)
    public void downloadPDFResource(HttpServletResponse response,
                                    @Param(value = "id") String id) throws IOException {

        FoafProfile foafProfile = foafProfileService.getFoafProfileByUri(id);
        File file = new File("./" + foafProfile.getProfileFile());
        if (file.exists()) {

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);

            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        }
    }

}
