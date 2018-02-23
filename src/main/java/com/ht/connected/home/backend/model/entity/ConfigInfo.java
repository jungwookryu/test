package com.ht.connected.home.backend.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;

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
	
}