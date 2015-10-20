package com.devry.mobile.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.devry.mobile.models.GpaModel;

@Component
public class BannerService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    @Qualifier("bannerapiJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    	
	public GpaModel getGpa(String dsi) {
        String gpa = jdbcTemplate.queryForObject("SELECT DEVRYACD.F_AS_OVERALL_GPA('849090', 'GR') gpa FROM sys.dual", String.class);
        log.debug(String.format("{%s : %s}", dsi, gpa));
		return new GpaModel(dsi, gpa);
	}

}