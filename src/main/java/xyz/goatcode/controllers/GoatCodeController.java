package xyz.goatcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goatcode.apis.Youtube;

@Controller
public class GoatCodeController {
    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("subpage", "index");
        model.addAttribute("subscribers", Youtube.subscribersCount);
        return "goatcode";
    }

    @RequestMapping("/kontakt")
    public String kontakt(Model model) {
        model.addAttribute("subpage", "kontakt");
        return "goatcode";
    }


    @RequestMapping("/projekty")
    public String projekty(Model model) {
        model.addAttribute("subpage", "projekty");
        return "goatcode";
    }
}
