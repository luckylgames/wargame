package com.luckyl.wargame.provider;

import com.luckyl.wargame.GameService;
import com.luckyl.wargame.PlayerService;
import lombok.extern.log4j.Log4j2;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
@EnableAutoConfiguration
@Log4j2
public class AppConfig {
    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        dataSource.setUrl("jdbc:h2:mem:testdb");

        return dataSource;
    }

    // TODO: Figure out why webservlet don't work
    @SuppressWarnings("rawtypes")
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}