package gguip1.community.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = System.getProperty("user.dir") + "/upload/images/";
        String uploadPath = Paths.get(uploadDir).toUri().toString(); // ✅ file:/C:/... 이런 형태로 변환

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

}
