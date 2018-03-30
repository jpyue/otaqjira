package gov.epa.otaq.fuel.service;

import gov.epa.otaq.fuel.model.OtaqCustomField;
import gov.epa.otaq.fuel.model.OtaqRegRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface OtaqCustomFieldRepository extends JpaRepository<OtaqCustomField, String> {

    public List<OtaqCustomField> findByDateCrCreatedBefore(Date date);

    public List<OtaqCustomField> findByDateCrCreatedBetween(Date start, Date end);

    public List<OtaqCustomField> findByCrStatusIn(List<String> active);

    public List<OtaqCustomField> findByCrStatusInAndCrTypeInAndLastUpdatedDateAfterOrderByLastUpdatedDateDesc(List<String> active, List<String> type, Date lastUpdated);

    /*public List<OtaqRegRequest> findByLastUpdatedAfter();*/
}
