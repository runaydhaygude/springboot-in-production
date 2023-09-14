package com.mdrsolutions.springbootprod.configurations;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(DBConfiguration.class)
public class DBConnectionConfiguration {

    private DBConfiguration dbConfiguration;

    public DBConnectionConfiguration(DBConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }

    @Profile("dev")
    @Bean
    public String devDBConnection() {
        System.out.println("DB Connection for DEV");
        System.out.println(dbConfiguration.getUrl());
        System.out.println(dbConfiguration.getDriverClassName());
        System.out.println(dbConfiguration.getUserName());
        System.out.println(dbConfiguration.getPassword());
        return "DB Connection for DEV";
    }

    @Profile("qa")
    @Bean
    public String qaDBConnection() {
        System.out.println("DB Connection for QA");
        System.out.println(dbConfiguration.getUrl());
        System.out.println(dbConfiguration.getDriverClassName());
        System.out.println(dbConfiguration.getUserName());
        System.out.println(dbConfiguration.getPassword());
        return "DB Connection for QA";
    }

    @Profile("prod")
    @Bean
    public String prodDBConnection() {
        System.out.println("DB Connection for PROD");
        System.out.println(dbConfiguration.getUrl());
        System.out.println(dbConfiguration.getDriverClassName());
        System.out.println(dbConfiguration.getUserName());
        System.out.println(dbConfiguration.getPassword());
        return "DB Connection for PROD";
    }
}
