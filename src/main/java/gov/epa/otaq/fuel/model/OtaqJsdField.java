package gov.epa.otaq.fuel.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by JYue on 1/23/2018.
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name="OTAQ_JSD_FIELD")
public class OtaqJsdField {
    @Id
    private int Id;
    private String jsdFieldName;
    private String jsdDataType;
    private String jsdIssueType;
    private String jsdRequired;
    private String crFieldName;
    private String crDataType;
    /*
    ID
    JSD_FIELD_NAME
    JSD_DATA_TYPE
    JSD_ISSUE_TYPE
    JSD_REQUIRED
    CR_FIELD_NAME
    CR_DATA_TYPE
    */

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getJsdFieldName() {
        return jsdFieldName;
    }

    public void setJsdFieldName(String jsdFieldName) {
        this.jsdFieldName = jsdFieldName;
    }

    public String getJsdDataType() {
        return jsdDataType;
    }

    public void setJsdDataType(String jsdDataType) {
        this.jsdDataType = jsdDataType;
    }

    public String getJsdIssueType() {
        return jsdIssueType;
    }

    public void setJsdIssueType(String jsdIssueType) {
        this.jsdIssueType = jsdIssueType;
    }

    public String getJsdRequired() {
        return jsdRequired;
    }

    public void setJsdRequired(String jsdRequired) {
        this.jsdRequired = jsdRequired;
    }

    public String getCrFieldName() {
        return crFieldName;
    }

    public void setCrFieldName(String crFieldName) {
        this.crFieldName = crFieldName;
    }

    public String getCrDataType() {
        return crDataType;
    }

    public void setCrDataType(String crDataType) {
        this.crDataType = crDataType;
    }

}
