package xyz.goatcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goatcode.apis.Socialblade;

@Controller
public class GoatCodeController {
    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("subpage", "index");
        model.addAttribute("subscribers", Socialblade.subscribersCount);
        return "goatcode";
    }

    @RequestMapping("/kontakt")
    public String kontakt(Model model) {
        model.addAttribute("subpage", "kontakt");
        return "goatcode";
    }

}