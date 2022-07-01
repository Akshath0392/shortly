package com.akshath.shortly.configs;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DBConfig {
    
    private Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    @Value("${app.shortly.datasource.driver-name}")
    private String driver;

    @Value("${app.shortly.datasource.username}")
    private String username;

    @Value("${app.shortly.datasource.password}")
    private String password;

    @Value("${app.shortly.datasource.url}")
    private String url;

    @Value("${app.shortly.datasource.platform}")
    private String platform;

    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("Datasource preparation ... STARTED");
        
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driver);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        log.info("Datasource preparation ... COMPLETED");
        return dataSourceBuilder.build();
    }
}
