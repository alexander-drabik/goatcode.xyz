package xyz.goatcode;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GoatCodeController {
    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("subpage", "index");
        return "goatcode";
    }

    @RequestMapping("/kontakt")
    public String kontakt(Model model) {
        model.addAttribute("subpage", "kontakt");
        return "goatcode";
    }
}
