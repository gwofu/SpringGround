package com.devry.mobile.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
public class BannerapiDataSourceConfiguration extends DataSourceAutoConfiguration {

	@Bean(name="bannerapiDataSource", destroyMethod="")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.bannerapi")
	public FactoryBean<?> bannerapiDataSource() {
        return new JndiObjectFactoryBean();
	}

	@Bean(name="bannerapiJdbcTemplate")
	public JdbcTemplate abnnerapiJdbcTemplate(DataSource bannerapiDataSource) {
		return new JdbcTemplate(bannerapiDataSource);
	}

}
