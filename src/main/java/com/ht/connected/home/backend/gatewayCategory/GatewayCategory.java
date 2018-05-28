package com.ht.connected.home.backend.gatewayCategory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "gateway_category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayCategory {

    @Id
    @Column(name = "no")
    private String no;

    @Column(name = "gateway_no")
    private int gatewayNo;

    @Column(name = "category")
    private String category;
    
    @Column(name = "category_no")
    private int categoryNo;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "nodeId")
    private int nodeId;
    
    @Column(name = "status")
    private String status;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "lastmodified_time")
    private Date lastmodifiedTime;

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
     * @return the gatewayNo
     */
    public int getGatewayNo() {
        return gatewayNo;
    }

    /**
     * @param gatewayNo the gatewayNo to set
     */
    public void setGatewayNo(int gatewayNo) {
        this.gatewayNo = gatewayNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the getCategoryNo
     */
    public int getCategoryNo() {
        return categoryNo;
    }

    /**
     * @param setCategoryNo the deviceSeq to set
     */
    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param active the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the createdTime
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the lastmodifiedTime
     */
    public Date getLastmodifiedTime() {
        return lastmodifiedTime;
    }

    /**
     * @param lastmodifiedTime the lastmodifiedTime to set
     */
    public void setLastmodifiedTime(Date lastmodifiedTime) {
        this.lastmodifiedTime = lastmodifiedTime;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "gatewayCategory [no=" + no + ", gatewayNo=" + gatewayNo + ", categoryNo=" + category + ", categoryNo="
                + categoryNo + ", nickname=" + nickname + ", Status=" + status + ", createdTime=" + createdTime
                + ", nodeId=" + nodeId
                + ", lastmodifiedTime=" + lastmodifiedTime + "]";
    }



}
