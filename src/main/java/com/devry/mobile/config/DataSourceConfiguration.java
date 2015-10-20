package com.devry.mobile.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
public class DataSourceConfiguration extends DataSourceAutoConfiguration {

	@Bean(name="bnrshareapiDataSource", destroyMethod="") 
	@ConfigurationProperties(prefix = "spring.datasource.bnrshareapi")
    public FactoryBean<?> bnrshareapiDataSource() { 
        return new JndiObjectFactoryBean();
    } 
 
//	@Bean(name = "bannerdevryadmapiDataSource") 
//	@ConfigurationProperties(prefix = "datasource.bannerdevryadmapi")
//    public DataSource bannerdevryadmapiDataSource() { 
//        return DataSourceBuilder.create().build(); 
//    } 

    @Bean(name="bnrshareapiJdbcTemplate") 
    public JdbcTemplate bnrshareapiJdbcTemplate(DataSource bnrshareapiDataSource) { 
        return new JdbcTemplate(bnrshareapiDataSource); 
    } 	

//    @Bean(name = "bnrshareapiJdbcTemplate") 
//    public JdbcTemplate bannerdevryadmapiJdbcTemplate(DataSource bannerdevryadmapiDataSource) { 
//        return new JdbcTemplate(bannerdevryadmapiDataSource); 
//    } 	
    
}
