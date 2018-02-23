package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Project : HT-CONNECTED-HOME-SERVER Package :
 * 
 * @auther : ijlee
 * @version : 1.0
 * @since : 2018.02.14
 */

@Entity
@Table(name = "config_info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigInfo {

	@Id
	@Column(name = "chenel_nm")
	private String chenelNm;
	
	@Column(name = "config_nm")
	private String configNm;
	
	@Column(name = "config_value")
	private String configValue;

	/**
	 * @return the chenelNm
	 */
	public String getChenelNm() {
		return chenelNm;
	}

	/**
	 * @param chenelNm the chenelNm to set
	 */
	public void setChenelNm(String chenelNm) {
		this.chenelNm = chenelNm;
	}

	/**
	 * @return the configNm
	 */
	public String getConfigNm() {
		return configNm;
	}

	/**
	 * @param configNm the configNm to set
	 */
	public void setConfigNm(String configNm) {
		this.configNm = configNm;
	}

	/**
	 * @return the configValue
	 */
	public String getConfigValue() {
		return configValue;
	}

	/**
	 * @param configValue the configValue to set
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfigInfo [chenelNm=" + chenelNm + ", configNm=" + configNm + ", configValue=" + configValue + "]";
	}
	
}