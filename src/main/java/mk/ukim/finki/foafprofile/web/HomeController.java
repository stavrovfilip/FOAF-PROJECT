package mk.ukim.finki.foafprofile.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"/home","/"})
public class HomeController {

    /**
     * Method for rendering home page
     * @param model
     * @param request
     * @return string - home page
     */
    @GetMapping
    public String getHomePage(Model model, HttpServletRequest request) {
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
}
