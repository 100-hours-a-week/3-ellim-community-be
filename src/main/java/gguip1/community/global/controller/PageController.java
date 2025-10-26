package gguip1.community.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/privacy")
    public String showPrivacyPage() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String showTermsPage() {
        return "terms";
    }
}
