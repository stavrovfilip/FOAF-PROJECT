package mk.ukim.finki.foafprofile.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.foafprofile.model.User;
import mk.ukim.finki.foafprofile.model.exceptions.InvalidUserCredentialException;
import mk.ukim.finki.foafprofile.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.foafprofile.service.EmailService;
import mk.ukim.finki.foafprofile.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    /**
     * Method for getting register page
     *
     * @param model
     * @return string - register page
     */
    @GetMapping("/register")
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);

        }
        model.addAttribute("bodyContent", "registerpage");
        return "master-template";
    }

    /**
     * Method for geting login page
     *
     * @param error
     * @param model
     * @return string - login page
     */
    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "loginpage");
        return "master-template";
    }

    /**
     * Method for registering new user on page
     *
     * @param firstname
     * @param lastname
     * @param username
     * @param password
     * @param repeatedPassword
     * @param email
     * @return stirng - redirecting page
     */
    @PostMapping("/register")
    public String register(@RequestParam String firstname,
                           @RequestParam String lastname,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String email) {
        try {
            User user = this.userService.register(username, email, firstname, lastname, password, repeatedPassword);
            this.emailService.sendMail(user);
            return "redirect:/login";

        } catch (InvalidUserCredentialException | PasswordsDoNotMatchException | MessagingException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }

    }

    /**
     * Method for login existing user on page
     *
     * @param request
     * @param model
     * @return string - redirecting home page or login page
     */
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
