package com.jkojote.weblib.config;

import com.jkojote.library.clauses.SqlClauseBuilder;
import com.jkojote.library.clauses.mysql.MySqlClauseBuilder;
import com.jkojote.library.config.PersistenceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.jkojote.weblib.application")
@Import({ThymeleafConfig.class, PersistenceConfig.class})
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/res/**").addResourceLocations("/res/");
    }

    @Bean
    public SqlClauseBuilder sqlClauseBuilder() {
        return new MySqlClauseBuilder();
    }
}
