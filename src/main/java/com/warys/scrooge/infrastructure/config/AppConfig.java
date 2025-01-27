package com.warys.scrooge.infrastructure.config;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.file.Path;

@Configuration
@EnableMongoAuditing
public class AppConfig {

    @Value("${ocr.data.path}")
    private String dataPath;
    @Value("${ocr.default.language}")
    private String defaultLanguage;

    @Bean
    public InternalResourceViewResolver resolver() {
        var resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }

    @Bean
    public ITesseract tesseract() {
        nu.pattern.OpenCV.loadLocally();

        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath(Path.of(dataPath).toAbsolutePath().toString());
        tesseract.setLanguage(defaultLanguage);

        return tesseract;
    }
}
