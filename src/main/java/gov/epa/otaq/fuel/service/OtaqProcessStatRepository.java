package gov.epa.otaq.fuel.service;

import gov.epa.otaq.fuel.model.OtaqProcessStat;
import gov.epa.otaq.fuel.model.OtaqValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface OtaqProcessStatRepository extends CrudRepository<OtaqProcessStat, Long> {
    public List<OtaqProcessStat> findAllByOrderByLastUpdatedDesc();
}
