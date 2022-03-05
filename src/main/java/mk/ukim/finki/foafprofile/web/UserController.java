package mk.ukim.finki.foafprofile.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.User;
import mk.ukim.finki.foafprofile.model.exceptions.InvalidUserCredentialException;
import mk.ukim.finki.foafprofile.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.foafprofile.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);

        }
        model.addAttribute("bodyContent", "registerpage");
        return "master-template";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "loginpage");
        return "master-template";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstname,
                           @RequestParam String lastname,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String email) {
        try {
            this.userService.register(username, email, firstname, lastname, password, repeatedPassword);
            return "redirect:/login";

        } catch (InvalidUserCredentialException | PasswordsDoNotMatchException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }

    }

    @PostMapping("/login")
    public String signIn(HttpServletRequest request, Model model) {
        User user = null;
        try {
            user = (User) this.userService.login(request.getParameter("username"), request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/home";
            //seuste ne napraeno
        } catch (InvalidUserCredentialException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("bodyContent", "loginpage");
            return "master-template";

        }

    }
}
