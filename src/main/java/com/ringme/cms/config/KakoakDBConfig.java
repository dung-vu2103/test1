package com.ringme.cms.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "kakoakEntityManagerFactory",
        transactionManagerRef = "kakoakTransactionManager",
        basePackages = {"com.ringme.cms.repository.kakoak"})
public class KakoakDBConfig {

    @Bean(name = "kakoakDataSourceProperties")
    @ConfigurationProperties("spring.kakoak-datasource")
    public DataSourceProperties videoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "kakoakDataSource")
    @ConfigurationProperties("spring.kakoak-datasource.configuration")
    public DataSource videoDataSource(@Qualifier("kakoakDataSourceProperties") DataSourceProperties kakoakDataSourceProperties) {
        return kakoakDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "kakoakEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean kakoakEntityManagerFactory(
            EntityManagerFactoryBuilder kakoakEntityManagerFactoryBuilder, @Qualifier("kakoakDataSource") DataSource videoDataSource) {

        Map<String, String> kakoakJpaProperties = new HashMap<>();
        kakoakJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        kakoakJpaProperties.put("hibernate.hbm2ddl.auto", "update");

        return kakoakEntityManagerFactoryBuilder
                .dataSource(videoDataSource)
                .packages("com.ringme.cms.model.kakoak")
                .persistenceUnit("kakoakDataSource")
                .properties(kakoakJpaProperties)
                .build();
    }

    @Bean(name = "kakoakTransactionManager")
    public PlatformTransactionManager kakoakTransactionManager(
            @Qualifier("kakoakEntityManagerFactory") EntityManagerFactory kakoakEntityManagerFactory) {

        return new JpaTransactionManager(kakoakEntityManagerFactory);
    }
}

