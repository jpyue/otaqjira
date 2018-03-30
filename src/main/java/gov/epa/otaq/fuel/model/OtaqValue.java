package gov.epa.otaq.fuel.model;

/**
 * Created by JYue on 1/29/2018.
 */

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="OTAQ_VALUE")
public class OtaqValue {

    @Id
    private int otaqValueId;
    private String code;
    private String value;
    private String category;
    private String activeYn;
/*
    OTAQ_VALUE_ID
    CODE
    VALUE
    CATEGORY
    ACTIVE_YN
*/

    public int getOtaqValueId() {
        return otaqValueId;
    }

    public void setOtaqValueId(int otaqValueId) {
        this.otaqValueId = otaqValueId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActiveYn() {
        return activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }
}
