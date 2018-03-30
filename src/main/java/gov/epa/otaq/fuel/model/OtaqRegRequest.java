package gov.epa.otaq.fuel.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by JYue on 1/22/2018.
 */
@Entity // This tells Hibernate to make a table out of this class
//@NamedQuery(name = "OTAQReq.findActive", query = "SELECT t FROM TempNewRequestVw t")
@Table(name="OTAQ_REG_REQUEST")
//@JsonRootName(value = "fields")
public class OtaqRegRequest {
    @Id
    @Column(name = "CR_ID")
    private long crId;
    @Column(name = "CR_TRACKING_NUMBER")
    private String crTrackingNumber;
    @Column(name = "USER_INFO_ID")
    private int userInfoId;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "USER_EMAIL_ADDRESS")
    private String userEmailAddress;
    @Column(name = "COMPANY_ID")
    private long CompanyId;
    @Column(name = "COMPANY_NAME")
    private String CompanyName;
    @Column(name = "CR_TYPE")
    private String crType;
    @Column(name = "CR_STATUS")
    private String crStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;
/*
    CR_ID
    CR_TRACKING_NUMBER
    USER_INFO_ID
    USER_ID
    USER_NAME
    USER_EMAIL_ADDRESS
    COMPANY_ID
    COMPANY_NAME
    CR_TYPE
    CR_STATUS
    CREATED_DATE
    LAST_UPDATED
*/

    public long getCrId() {
        return crId;
    }

    public void setCrId(int crId) {
        this.crId = crId;
    }

    public String getCrTrackingNumber() {
        return crTrackingNumber;
    }

    public void setCrTrackingNumber(String crTrackingNumber) {
        this.crTrackingNumber = crTrackingNumber;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public long getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(long companyId) {
        CompanyId = companyId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCrType() {
        return crType;
    }

    public void setCrType(String crType) {
        this.crType = crType;
    }

    public String getCrStatus() {
        return crStatus;
    }

    public void setCrStatus(String crStatus) {
        this.crStatus = crStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
