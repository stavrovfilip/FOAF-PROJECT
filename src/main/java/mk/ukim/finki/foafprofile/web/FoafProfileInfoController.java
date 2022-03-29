package mk.ukim.finki.foafprofile.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.*;
import mk.ukim.finki.foafprofile.model.dto.FriendDto;
import mk.ukim.finki.foafprofile.service.*;
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
    private final UserService userService;
    private final PictureService pictureService;

    /**
     * Method for getting page for creating new FOAF profile
     *
     * @param model
     * @return
     */
    @GetMapping("/foafprofile/create")
    public String getFoafProfileCreatePage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "createFoafPofile");

        return "master-template";
    }

    /**
     * Method for updating already created FOAF profile
     *
     * @param id
     * @param model
     * @return string - page
     */
    @GetMapping("/foafprofile/edit/{id}")
    public String editFoafProfilePage(@PathVariable String id, Model model) {

        if (this.foafProfileInfoService.findByURI(id) != null) {
            FoafProfileInfo foafProfileInfo = this.foafProfileInfoService.findByURI(id);
            model.addAttribute("foafProfileInfo", foafProfileInfo);
            model.addAttribute("bodyContent", "createFoafPofile");
            return "master-template";
        }
        return "redirect:/profiles?error=FoafProfileNotFound";
    }

    /**
     * Method for creating new FOAF profile in FOAF info part and FOAF profile part
     *
     * @param id
     * @param title
     * @param firstName
     * @param lastName
     * @param nickName
     * @param email
     * @param homepage
     * @param phoneNumber
     * @param picture
     * @param workHomepage
     * @param workDescription
     * @param schoolHomepage
     * @param friendFirstName
     * @param friendLastName1
     * @param friendEmail
     * @param friendFoafUri
     * @param friendFirstName2
     * @param friendLastName2
     * @param friendEmail2
     * @param friendFoafUri2
     * @param friendFirstName3
     * @param friendLastName3
     * @param friendEmail3
     * @param friendFoafUri3
     * @param req
     * @return string - redirecting page
     * @throws IOException
     */
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
                    picture1, workHomepage, workDescription, schoolHomepage, friends, username);
        }


        return "redirect:/profile";

    }

    /**
     * Method for getting already created FOAF profile for site user
     *
     * @param request
     * @param model
     * @return string - page
     */
    @GetMapping("/profile")
    public String showFoafProfile(HttpServletRequest request, Model model) {
        String username = request.getRemoteUser();
        FoafProfile foafProfile = userService.findFoafProfileByUsername(username);
        model.addAttribute("profile", foafProfile);
        model.addAttribute("signinuser", username);
        model.addAttribute("foafprofileowner", username);
        model.addAttribute("bodyContent", "myProfile");

        return "master-template";

    }

    /**
     * Methnod for getting already created FOAF profile with associated id
     *
     * @param id
     * @param model
     * @return string - page
     */
    @GetMapping("/profile/{id}")
    public String showFoafProfileById(@PathVariable String id, Model model, HttpServletRequest request) {
        FoafProfile foafProfile = foafProfileService.getFoafProfileByUri(id);
        String username = request.getRemoteUser();
        String foafProfileOwner = this.userService.findUserByFoafProfile(foafProfile).getUsername();
        model.addAttribute("profile", foafProfile);
        model.addAttribute("signinuser", username);
        model.addAttribute("foafprofileowner", foafProfileOwner);

        model.addAttribute("bodyContent", "myProfile");

        return "master-template";

    }

    /**
     * Method for deleting already existing FOAF profile
     *
     * @param id
     * @param req
     * @return string - redirecting page
     */
    @DeleteMapping("/foafprofile/delete/{id}")
    public String deleteFoafProfile(@PathVariable String id, HttpServletRequest req) {
        //deleting FOAF profile
        String username = req.getRemoteUser();
        this.foafProfileService.deleteFoafProfile(id, username);
        this.foafProfileInfoService.deleteProfile(id, username);

        return "redirect:/profiles";
    }

    /**
     * Method for downloading rdf FOAF profile from site
     *
     * @param response
     * @param id
     * @throws IOException
     */
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

    /**
     * Method for converting already created FOAF profile from rdf to provided type and downloading it
     *
     * @param response
     * @param id
     * @param format
     * @throws IOException
     */
    @RequestMapping(value = "/foafprofile/converter", method = RequestMethod.GET)
    public void downloadConveredResource(HttpServletResponse response,
                                         @Param(value = "id") String id, @Param(value = "format") String format) throws IOException {

        File file = new File("./" + foafProfileService.convertFromRDF(id, format));
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
