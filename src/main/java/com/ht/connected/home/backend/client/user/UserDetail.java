package com.ht.connected.home.backend.client.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author COM
 *
 */
public class UserDetail implements UserDetails {

	private String password;

	private String nickName;
	private String userEmail;
	private String active;
	private boolean admin;
	private boolean isPasswordSet;
	private Locale locale;
	private List<String> roles;

	private String passwordSet;
	private String status;
	private String userDesc;

	private boolean accountNonExpired, accountNonLocked, credentialsNonExpired, enabled;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private List<GrantedAuthority> authorities = new ArrayList();

	public UserDetail() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
	}

	public UserDetail(User users) {
		logger.debug("DashboardAuthenticationDetails start");
		this.password = users.getRePassword();
		this.nickName = users.getNickName();
		this.userEmail = users.getUserEmail();
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
		this.authorities.add(new SimpleGrantedAuthority(users.getAuthority()));
		this.authorities.add(new SimpleGrantedAuthority(UserActive.values()[users.getActive()].name()));
		logger.debug("DashboardAuthenticationDetails end");
	}

	public void grantAuthority(String authority) {
		if (roles == null)
			roles = new ArrayList<>();
		roles.add(authority);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
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
	 * @param userEmail
	 *            the userEmail to set
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
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
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
	 * @param isPasswordSet
	 *            the isPasswordSet to set
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
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * @return the passwordSet
	 */
	public String getPasswordSet() {
		return passwordSet;
	}

	/**
	 * @param passwordSet
	 *            the passwordSet to set
	 */
	public void setPasswordSet(String passwordSet) {
		this.passwordSet = passwordSet;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the userDesc
	 */
	public String getUserDesc() {
		return userDesc;
	}

	/**
	 * @param userDesc
	 *            the userDesc to set
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
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @param accountNonExpired
	 *            the accountNonExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @param accountNonLocked
	 *            the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @param credentialsNonExpired
	 *            the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.userEmail;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserDetail [password=" + password + ", nickName=" + nickName + ", userEmail=" + userEmail + ", active=" + active + ", admin=" + admin + ", isPasswordSet=" + isPasswordSet + ", locale="
                + locale + ", roles=" + roles + ", passwordSet=" + passwordSet + ", status=" + status + ", userDesc=" + userDesc + ", accountNonExpired=" + accountNonExpired + ", accountNonLocked="
                + accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + ", logger=" + logger + ", authorities=" + authorities + "]";
    }

}
