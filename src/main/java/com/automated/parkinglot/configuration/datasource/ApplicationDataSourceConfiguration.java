package com.automated.parkinglot.configuration.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "applicationEntityManagerFactory",
    transactionManagerRef = "applicationTransactionManager",
    basePackages = "com.automated.parkinglot.repository.application")
public class ApplicationDataSourceConfiguration {

  @Bean(name = "applicationDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.application")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean(name = "applicationDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.application")
  public DataSource dataSource() {
    return dataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean(name = "applicationEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
    return entityManagerFactoryBuilder
        .dataSource(dataSource)
        .packages("com.automated.parkinglot.models.application")
        .build();
  }

  @Bean(name = "applicationTransactionManager")
  public TransactionManager transactionManager(
          @Qualifier("applicationEntityManagerFactory")EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
