package com.ht.connected.home.backend.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * user_gateway 테이블 엔터티
 * @author 구정화
 *
 */
@Entity
@Table(name = "user_gateway")
public class UserGateway {
	@Id
	@Column(name = "no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int no;

	@Column(name = "user_no", nullable = false, unique = true)
	private int userNo;

	@Column(name = "group_no")
	private int groupNo;

	@Column(name = "gateway_no")
	private int gatewayNo;

	@Column(name = "group_role")
	private String groupRole;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public int getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public int getGatewayNo() {
		return gatewayNo;
	}

	public void setGatewayNo(int gatewayNo) {
		this.gatewayNo = gatewayNo;
	}

	public String getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}



}