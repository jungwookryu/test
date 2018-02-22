package com.ht.connected.home.backend.model.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Project : HT-CONNECTED-HOME-SERVER Package :
 * com.ht.connected.home.front.model.entity File : Users.java Description : 사용자
 * 
 * @auther : ijlee
 * @version : 1.0
 * @since : 2018.02.14
 */

@Entity
@Table(name = "common_code")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonCode {

	@Id
	@NotNull
	@Column(name = "no")
	private String no;
	
	@Column(name = "code_id")
	private String codeId;
	
	@Column(name = "code_group_id")
	private String codeGroupId;
	
	@Column(name = "code_val")
	private String codeVal;
	
	@Column(name = "code_group_name")
	private String codeGroupName;
	
	@Column(name = "code_name")
	private String codeName;
	
	@Column(name = "code_description")
	private String codeDescription;
			
	@Column(name = "code_type")
	private String codeType;
	
	
}