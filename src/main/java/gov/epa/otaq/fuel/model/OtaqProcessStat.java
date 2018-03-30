package gov.epa.otaq.fuel.model;

/**
 * Created by JYue on 1/29/2018.
 */

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="OTAQ_PROCESS_STAT")
//@SequenceGenerator(name="OTAQ_PROCESS_STAT_SEQ", initialValue=1, allocationSize=10)
//@NamedQuery(name="OTAQ_PROCESS_STAT.findLastUpdate", query="SELECT o FROM OTAQ_PROCESS_STAT o")
public class OtaqProcessStat {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name="OTAQ_PROCESS_STAT_ID", sequenceName="OTAQ_PROCESS_STAT_SEQ", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OTAQ_PROCESS_STAT_ID")
    private long  otaqProcessStatId;
    private Date lastUpdated;
    private int  totalInsert;
    private int  totalUpdate;
    private int  statusUpdate;
    private int  newRecord;
/*
    OTAQ_PROCESS_STAT_ID
	LAST_UPDATED
	TOTAL_INSERT
	TOTAL_UPDATE
*/

    public long getOtaqProcessStatId() {
        return otaqProcessStatId;
    }

    public void setOtaqProcessStatId(long otaqProcessStatId) {
        this.otaqProcessStatId = otaqProcessStatId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getTotalInsert() {
        return totalInsert;
    }

    public void setTotalInsert(int totalInsert) {
        this.totalInsert = totalInsert;
    }

    public int getTotalUpdate() {
        return totalUpdate;
    }

    public void setTotalUpdate(int totalUpdate) {
        this.totalUpdate = totalUpdate;
    }

    public int getNewRecord() {
        return newRecord;
    }

    public void setNewRecord(int newRecord) {
        this.newRecord = newRecord;
    }

    public int getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(int statusUpdate) {
        this.statusUpdate = statusUpdate;
    }
}
