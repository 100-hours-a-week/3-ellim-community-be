package gguip1.community.global.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles; // <-- import 추가
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HealthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev-local")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void health_should_return_ok() throws Exception {
        mockMvc.perform(get("/health2"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}