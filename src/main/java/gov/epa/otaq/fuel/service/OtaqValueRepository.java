package gov.epa.otaq.fuel.service;

import gov.epa.otaq.fuel.model.OtaqValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface OtaqValueRepository extends JpaRepository<OtaqValue, Long> {
    public OtaqValue findByCode(String url);

    public List<OtaqValue> findByCategory(String active);
}
