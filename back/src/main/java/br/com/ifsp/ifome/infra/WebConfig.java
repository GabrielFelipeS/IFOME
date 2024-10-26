package br.com.ifsp.ifome.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String PATH_TO_FOLDER_IMAGES = System.getProperty("user.dir") + "/src/main/resources/static/images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/image/**")
            .addResourceLocations("file:" + PATH_TO_FOLDER_IMAGES);
    }
}