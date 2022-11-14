package com.security.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This might help to remove the routing problem in Rreact (setting route url by hand or refreshin route page)
 * The configuration redirects all the routing to domain root to be handled by the React index.html
 */

@Configuration
public class WebConfiguration implements WebMvcConfigurer{

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring://w+}").setViewName("forward:/");
        registry.addViewController("/*/{spring://w+}").setViewName("forward:/");
    }
    
}
