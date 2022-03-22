package mk.ukim.finki.foafprofile.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.FoafProfile;
import mk.ukim.finki.foafprofile.model.User;
import mk.ukim.finki.foafprofile.service.FoafProfileService;
import mk.ukim.finki.foafprofile.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/profiles")
public class AllFoafProfilesController {
    private final FoafProfileService foafProfileService;
    private final UserService userService;

    @GetMapping
    public String getAllFoafProfilesPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<User> users = this.userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("bodyContent", "allFoafProfiles");
        return "master-template";
    }

}
