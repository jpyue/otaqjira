package gov.epa.otaq.fuel.service;


import gov.epa.otaq.fuel.model.OtaqCustomField;
import gov.epa.otaq.fuel.model.OtaqRegRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by JYue on 1/23/2018.
 */
@Service
public class OtaqCustomFieldService {

    @Autowired
    private OtaqCustomFieldRepository otaqCustomFieldRepository;

    @Autowired
    private OtaqValueService otaqValueService;

    public Date getLastUpdated(Date dt) {
        List<OtaqCustomField> ocs = getOtaqCustomFieldLastUpdatedDateAfterDesc(dt);
        for(OtaqCustomField op : ocs){
            return op.getLastUpdatedDate();
        }

        return null;
    }

    public List<OtaqCustomField> getOtaqCustomFieldLastUpdatedDateAfterDesc(Date dt) {
        List<String> active = otaqValueService.getAllActive();
        List<String> type = otaqValueService.getValidType();
        return otaqCustomFieldRepository.findByCrStatusInAndCrTypeInAndLastUpdatedDateAfterOrderByLastUpdatedDateDesc(active,type, dt);
    }
}
