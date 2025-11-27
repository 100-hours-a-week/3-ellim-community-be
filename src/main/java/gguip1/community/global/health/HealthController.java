package gguip1.community.global.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health2")
    public String healthCheck() {
        return "OK";
    }
}
