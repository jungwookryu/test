package com.ht.connected.home.backend.config.security;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 * @author COM
 *
 */
public class UserDetails extends OAuth2AuthenticationDetails{
	
	
	private final String no;
    private final String userId;
    private final String password;
    
    private String username;
    private String nickName;
    private String userEmail;
    private String active;
    private List<String> authorities;
    private String email;
    private boolean admin;
    private boolean isPasswordSet;
    private Locale locale;
    
    private String passwordSet;
    private String status;
    private String userDesc;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    public UserDetails(HttpServletRequest request, String no, String userId, String password) {
        super(request);
        logger.debug("DashboardAuthenticationDetails start");
        this.no = no;
        this.userId = userId;
        this.password = password;
        logger.debug("DashboardAuthenticationDetails end");
    }

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the authorities
	 */
	public List<String> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the isPasswordSet
	 */
	public boolean isPasswordSet() {
		return isPasswordSet;
	}

	/**
	 * @param isPasswordSet the isPasswordSet to set
	 */
	public void setPasswordSet(boolean isPasswordSet) {
		this.isPasswordSet = isPasswordSet;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the userDesc
	 */
	public String getUserDesc() {
		return userDesc;
	}

	/**
	 * @param userDesc the userDesc to set
	 */
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the passwordSet
	 */
	public String getPasswordSet() {
		return passwordSet;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserDetails [no=" + no + ", userId=" + userId + ", password=" + password + ", username=" + username
				+ ", nickName=" + nickName + ", userEmail=" + userEmail + ", active=" + active + ", authorities="
				+ authorities + ", email=" + email + ", admin=" + admin + ", isPasswordSet=" + isPasswordSet
				+ ", locale=" + locale + ", passwordSet=" + passwordSet + ", status=" + status + ", userDesc="
				+ userDesc + ", logger=" + logger + "]";
	}
	
	
}
