package com.example.demo.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurationSupport {
    @Autowired
    FilePathConfig filePathConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
//        String pre ="file:///";
        String pre ="file:";
        registry.addResourceHandler("/images/**").
                addResourceLocations(pre+filePathConfig.getFileUrl()+filePathConfig.getImagePath());
        registry.addResourceHandler("/videos/**").
                addResourceLocations(pre+filePathConfig.getFileUrl()+filePathConfig.getVideoPath());
        registry.addResourceHandler("/enclosure/**").
                addResourceLocations(pre+filePathConfig.getFileUrl()+filePathConfig.getEnclosurePath());
        super.addResourceHandlers(registry);
    }

}

