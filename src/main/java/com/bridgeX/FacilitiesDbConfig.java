package com.bridgeX;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.bridgeX.facilities",
        entityManagerFactoryRef = "facilityEntityManagerFactory",
        transactionManagerRef = "facilityTransactionManager"
)
public class FacilitiesDbConfig {

    @Bean(name = "facilityDataSource")
    public DataSource facilityDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mariadb://localhost:3306/brangx");
        ds.setUsername("bridge_user");
        ds.setPassword("1234");
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        return ds;
    }

    @Bean(name = "facilityEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean facilityEntityManagerFactory(
            @Qualifier("facilityDataSource") DataSource facilityDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(facilityDataSource);
        em.setPackagesToScan("com.bridgeX.facilities");
        em.setPersistenceUnitName("facility");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> jpaProps = new HashMap<>();
        jpaProps.put("hibernate.hbm2ddl.auto", "none");
        jpaProps.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        jpaProps.put("hibernate.show_sql", true);
        em.setJpaPropertyMap(jpaProps);

        return em;
    }

    @Bean(name = "facilityTransactionManager")
    public PlatformTransactionManager facilityTransactionManager(
            @Qualifier("facilityEntityManagerFactory")
            EntityManagerFactory facilityEntityManagerFactory
    ) {
        return new JpaTransactionManager(facilityEntityManagerFactory);
    }
}
