package com.github.blog.config;

import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Raman Haurylau
 */
@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement(proxyTargetClass = true)
public class PersistenceJPAConfig {

    @Bean
    @DependsOn("liquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSourceProperties dataSourceProperties, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.github.blog.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", dataSourceProperties.getHibernateDdlAuto());
//
//        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public SpringLiquibase liquibase(DataSourceProperties dataSourceProperties, DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setChangeLog(dataSourceProperties.getChangelogFile());
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
