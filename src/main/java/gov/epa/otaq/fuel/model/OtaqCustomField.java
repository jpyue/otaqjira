package gov.epa.otaq.fuel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name="OTAQ_CUSTOM_FIELD")
@JsonIgnoreProperties(value = {"changeRequiresEr","assignee","timeInStatus","publicStatus","internalWorkflowStatus",
        "attachment","dueDate","description","priority","labels","approvers","epaActivated","rtTicketNumber",
        "apexMailLogId","linkedIssues","natureOfChange","fuelType","feedstock","dateRcoEsigned","dateCrPaperworkReceived",
        "activationDate","crType"})
public class OtaqCustomField {
    @Id
    private String summary;
    private String changeRequiresEr;
    private String assignee;
    private String companyId;
    private String companyName;
    private String submitterName;
    private String submitterEmailAddress;
    @Column(name = "CR_STATUS")
    private String crStatus;
    private String timeInStatus;
    private String publicStatus;
    private String internalWorkflowStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date dateCrCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date lastUpdatedDate;
    private String attachment;
    private Date dueDate;
    private String description;
    private String priority;
    private String labels;
    private String approvers;
    private String epaActivated;
    private String rtTicketNumber;
    private String apexMailLogId;
    private String linkedIssues;
    private String natureOfChange;
    private String fuelType;
    private String feedstock;
    private Date dateRcoEsigned;
    private Date dateCrPaperworkReceived;
    private Date activationDate;
    private String crType;

    public String getCrType() {
        return crType;
    }

    public void setCrType(String crType) {
        this.crType = crType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getChangeRequiresEr() {
        return changeRequiresEr;
    }

    public void setChangeRequiresEr(String changeRequiresEr) {
        this.changeRequiresEr = changeRequiresEr;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getSubmitterEmailAddress() {
        return submitterEmailAddress;
    }

    public void setSubmitterEmailAddress(String submitterEmailAddress) {
        this.submitterEmailAddress = submitterEmailAddress;
    }

    public String getCrStatus() {
        return crStatus;
    }

    public void setCrStatus(String crStatus) {
        this.crStatus = crStatus;
    }

    public String getTimeInStatus() {
        return timeInStatus;
    }

    public void setTimeInStatus(String timeInStatus) {
        this.timeInStatus = timeInStatus;
    }

    public String getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(String publicStatus) {
        this.publicStatus = publicStatus;
    }

    public String getInternalWorkflowStatus() {
        return internalWorkflowStatus;
    }

    public void setInternalWorkflowStatus(String internalWorkflowStatus) {
        this.internalWorkflowStatus = internalWorkflowStatus;
    }

    public Date getDateCrCreated() {
        return dateCrCreated;
    }

    public void setDateCrCreated(Date dateCrCreated) {
        this.dateCrCreated = dateCrCreated;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public String getEpaActivated() {
        return epaActivated;
    }

    public void setEpaActivated(String epaActivated) {
        this.epaActivated = epaActivated;
    }

    public String getRtTicketNumber() {
        return rtTicketNumber;
    }

    public void setRtTicketNumber(String rtTicketNumber) {
        this.rtTicketNumber = rtTicketNumber;
    }

    public String getApexMailLogId() {
        return apexMailLogId;
    }

    public void setApexMailLogId(String apexMailLogId) {
        this.apexMailLogId = apexMailLogId;
    }

    public String getLinkedIssues() {
        return linkedIssues;
    }

    public void setLinkedIssues(String linkedIssues) {
        this.linkedIssues = linkedIssues;
    }

    public String getNatureOfChange() {
        return natureOfChange;
    }

    public void setNatureOfChange(String natureOfChange) {
        this.natureOfChange = natureOfChange;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFeedstock() {
        return feedstock;
    }

    public void setFeedstock(String feedstock) {
        this.feedstock = feedstock;
    }

    public Date getDateRcoEsigned() {
        return dateRcoEsigned;
    }

    public void setDateRcoEsigned(Date dateRcoEsigned) {
        this.dateRcoEsigned = dateRcoEsigned;
    }

    public Date getDateCrPaperworkReceived() {
        return dateCrPaperworkReceived;
    }

    public void setDateCrPaperworkReceived(Date dateCrPaperworkReceived) {
        this.dateCrPaperworkReceived = dateCrPaperworkReceived;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }
}
