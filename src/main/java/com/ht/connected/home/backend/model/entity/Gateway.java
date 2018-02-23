package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gateway")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gateway {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "no")
	private String no;

	@Column(name = "user_no")
	private String userNo;
	
	@Column(name = "group_no")
	private String groupNo;
	
	@Column(name = "gateway_no")
	private String gatewayNo;
	
	@Column(name = "group_role")
	private String groupRole;

	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}

	/**
	 * @return the userNo
	 */
	public String getUserNo() {
		return userNo;
	}

	/**
	 * @param userNo the userNo to set
	 */
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	/**
	 * @return the groupNo
	 */
	public String getGroupNo() {
		return groupNo;
	}

	/**
	 * @param groupNo the groupNo to set
	 */
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	/**
	 * @return the gatewayNo
	 */
	public String getGatewayNo() {
		return gatewayNo;
	}

	/**
	 * @param gatewayNo the gatewayNo to set
	 */
	public void setGatewayNo(String gatewayNo) {
		this.gatewayNo = gatewayNo;
	}

	/**
	 * @return the groupRole
	 */
	public String getGroupRole() {
		return groupRole;
	}

	/**
	 * @param groupRole the groupRole to set
	 */
	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Gateway [no=" + no + ", userNo=" + userNo + ", groupNo=" + groupNo + ", gatewayNo=" + gatewayNo
				+ ", groupRole=" + groupRole + ", getNo()=" + getNo() + ", getUserNo()=" + getUserNo()
				+ ", getGroupNo()=" + getGroupNo() + ", getGatewayNo()=" + getGatewayNo() + ", getGroupRole()="
				+ getGroupRole() + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gatewayNo == null) ? 0 : gatewayNo.hashCode());
		result = prime * result + ((groupNo == null) ? 0 : groupNo.hashCode());
		result = prime * result + ((groupRole == null) ? 0 : groupRole.hashCode());
		result = prime * result + ((no == null) ? 0 : no.hashCode());
		result = prime * result + ((userNo == null) ? 0 : userNo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gateway other = (Gateway) obj;
		if (gatewayNo == null) {
			if (other.gatewayNo != null)
				return false;
		} else if (!gatewayNo.equals(other.gatewayNo))
			return false;
		if (groupNo == null) {
			if (other.groupNo != null)
				return false;
		} else if (!groupNo.equals(other.groupNo))
			return false;
		if (groupRole == null) {
			if (other.groupRole != null)
				return false;
		} else if (!groupRole.equals(other.groupRole))
			return false;
		if (no == null) {
			if (other.no != null)
				return false;
		} else if (!no.equals(other.no))
			return false;
		if (userNo == null) {
			if (other.userNo != null)
				return false;
		} else if (!userNo.equals(other.userNo))
			return false;
		return true;
	}
}
