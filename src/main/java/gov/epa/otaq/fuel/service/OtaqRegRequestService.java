package gov.epa.otaq.fuel.service;


import gov.epa.otaq.fuel.model.OtaqRegRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by JYue on 1/23/2018.
 */
@Service
public class OtaqRegRequestService {

    @Autowired
    private OtaqRegRequestRepository otaqRegRequestRepository;

    @Autowired
    private OtaqValueService otaqValueService;
    /*public Date getLastUpdated() {
        return otaqRegRequestRepository.findByLastUpdatedAfter().iterator().next().getLastUpdated();
    }*/

    public List<OtaqRegRequest> getOtaqRegRequestsLastUpdatedAfter(Date dt) {
        List<String> active = otaqValueService.getAllActive();
        return otaqRegRequestRepository.findByCrStatusInAndLastUpdatedAfterOrderByLastUpdatedAsc(active, dt);
    }
}
