package gov.epa.otaq.fuel.service;


import gov.epa.otaq.fuel.model.OtaqValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYue on 1/23/2018.
 */
@Service
public class OtaqValueService {
    private String ACTIVE_REQUEST = "activeCr";
    private String VALID_TYPE = "validType";

    @Autowired
    private OtaqValueRepository otaqValueRepository;

    public String getUrlByCode(String urlCode) {
        return otaqValueRepository.findByCode(urlCode).getValue();
    }

    public List<String> getAllActive() {
        List<String> activelist = new ArrayList<>();

        List<OtaqValue> ovs = otaqValueRepository.findByCategory(ACTIVE_REQUEST);
        for(OtaqValue ov : ovs) {
            activelist.add(ov.getValue());
        }

        return activelist;
    }

    public List<String> getValidType() {
        List<String> typelist = new ArrayList<>();

        List<OtaqValue> ovs = otaqValueRepository.findByCategory(VALID_TYPE);
        for(OtaqValue ov : ovs) {
            typelist.add(ov.getValue());
        }

        return typelist;
    }
}
