package com.devry.mobile.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 *
 * ConnectionHelper.java
 * Helper class to get a Connection from the Datasource Connection Pool.
 * connection details are available in a resource file in the project.
 * Requires the jndi name and the datasource context to create a new connection.
 * The Methods read a 'db.properties' file in the build path to retrieve the context and jndiNames for the
 * datasources.
 *
 * @author Visali Dole
 *
 */
public class ConnectionHelper {
	private static final Logger log = Logger.getLogger(ConnectionHelper.class);
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
	private static final String BANNER_CONTEXT = resourceBundle.getString("banner_context");
	private static final String STUDENT_BANNER_JNDINAME = resourceBundle.getString("student_banner_jndiName");
	private static final String BANNER_SHARED_API_JNDINAME = resourceBundle.getString("banner_shared_api_jndiName");
	private static DataSource bannerDataSource = null;
	private static DataSource bannerSharedDataSource = null;
	
	
	/**
	 * Method gets connection Object from the Datasource configured.
	 *
	 *
	 * @return java.sql.Connection
	 *
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static synchronized Connection getStudentBannerConnection() throws BannerConnectionException {
		
		try{
			if (bannerDataSource == null) {
				InitialContext mContext= new InitialContext();
				Context envContext= (Context) mContext.lookup(BANNER_CONTEXT);
				bannerDataSource = (DataSource) envContext.lookup(STUDENT_BANNER_JNDINAME);
			}
			log.debug("Connection Helper Class Got Connection Object successfully.");
			return bannerDataSource.getConnection();
			
		}catch (SQLException e) {			
			log.error("Error while getting Banner connection for student." + e);
			throw new BannerConnectionException();
		}catch (NamingException e) {
			//warning
			log.warn("Error while getting Banner connection for student." + e);
			throw new BannerConnectionException();
		}
		
	}

	public static synchronized DataSource getBannerSharedDataSource()  {
		
		if (bannerSharedDataSource == null) {
			try {
				
				Context envContext = (Context) new InitialContext().lookup(BANNER_CONTEXT);
				bannerSharedDataSource = (DataSource) envContext.lookup(BANNER_SHARED_API_JNDINAME);
				
			} catch (NamingException e) {
				log.warn("NamingException" + e);
				e.printStackTrace();
			}			
		}
		
		return bannerSharedDataSource;
	}
	
	public static synchronized DataSource getBannerDataSource()  {
		
		if (bannerDataSource == null) {
			try {
				
				Context envContext = (Context) new InitialContext().lookup(BANNER_CONTEXT);
				bannerDataSource = (DataSource) envContext.lookup(STUDENT_BANNER_JNDINAME);
				
			} catch (NamingException e) {
				log.warn("NamingException" + e);
			}			
		}
		
		return bannerDataSource;
	}
}
