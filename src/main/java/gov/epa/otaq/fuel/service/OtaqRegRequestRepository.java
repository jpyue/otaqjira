package gov.epa.otaq.fuel.service;

import gov.epa.otaq.fuel.model.OtaqRegRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.NamedQuery;
import java.util.Date;
import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface OtaqRegRequestRepository extends JpaRepository<OtaqRegRequest, Long> {

    public List<OtaqRegRequest> findByCreatedDateBefore(Date date );

    public List<OtaqRegRequest> findByCreatedDateBetween(Date start, Date end);

    public List<OtaqRegRequest> findByCrStatusIn(List<String> active);

    public List<OtaqRegRequest> findByCrStatusInAndLastUpdatedAfterOrderByLastUpdatedAsc(List<String> active, Date lastUpdated);

    /*public List<OtaqRegRequest> findByLastUpdatedAfter();*/
}
