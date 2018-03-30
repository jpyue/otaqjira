package gov.epa.otaq.fuel.service;


import gov.epa.otaq.fuel.model.OtaqProcessStat;
import gov.epa.otaq.fuel.model.OtaqValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JYue on 1/23/2018.
 */
@Service
public class OtaqProcessStatService {
    @Autowired
    private OtaqProcessStatRepository otaqProcessStatRepository;

    public Date getLastUpdated(){
        Date lastUpdate = null;
        List<OtaqProcessStat> ops = otaqProcessStatRepository.findAllByOrderByLastUpdatedDesc();
        for(OtaqProcessStat op : ops){
            lastUpdate = op.getLastUpdated();
            break;
        }

        return lastUpdate;
    }
}
