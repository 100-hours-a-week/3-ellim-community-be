package gguip1.community.global.controller;

import gguip1.community.global.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @GetMapping("/terms")
    public String showTermsPage(Model model) {
        model.addAttribute("termsContent", pageService.getTermsContent());
        return "terms";
    }

    @GetMapping("/privacy")
    public String showPrivacyPage(Model model) {
        model.addAttribute("privacyContent", pageService.getPrivacyContent());
        return "privacy";
    }
}
