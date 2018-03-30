package gov.epa.otaq.fuel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import gov.epa.otaq.fuel.model.OtaqJsdField;
import gov.epa.otaq.fuel.model.OtaqCustomField;
import gov.epa.otaq.fuel.model.OtaqProcessStat;
import gov.epa.otaq.fuel.service.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.json.JsonObject;
//import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JYue on 1/7/2018.
 */
@Component
public class ScheduleSync {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleSync.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    static private String USER_NAME = "name";
    static private String USER_PW   = "password";
    static private String SEARCH    = "searchUrl";
    static private String INFO      = "infoUrl";
    static private String PROJECT_KEY_URL    = "prjKeyUrl";
    static private String CR_TYPE    = "typeUrl";
    static private String FIELD    = "fieldUrl";
    static private String ISSUE    = "issueUrl";
    static private String PROJECT_KEY    = "projectKey";

    HashMap<String, String> colkeys = new HashMap();
    HashMap<String, String> fieldkeys = new HashMap();
    HashMap<String, String> optionkeys = new HashMap();
    protected long dateTime;
    protected Date lastUpdated;
    protected GregorianCalendar calendar;
 // n   protected Timestamp timestamp;
    private List<OtaqJsdField> jfs;
    private Date lastProcessed;
    private String authStringEnc;
    int totalInsert = 0;
    int totalUpdate = 0;
    int statusUpdate = 0;
    int newRecord = 0;

    @Autowired
    private OtaqCustomFieldRepository otaqCustomFieldRepository;

    @Autowired
    private OtaqJsdFieldRepository otaqJsdFieldRepository;

    @Autowired
    OtaqCustomFieldService otaqCustomFieldService;

    @Autowired
    OtaqValueRepository otaqValueRepository;

    @Autowired
    OtaqValueService otaqValueService;

    @Autowired
    OtaqProcessStatRepository otaqProcessStatRepository;

    @Autowired
    OtaqProcessStatService otaqProcessStatService;

    //@Scheduled(fixedRate = 20000000)
    //@Scheduled(cron = "0 0 6 * * *")
    @Scheduled(cron = "${cronschedule}")
    public void scheduleTask() {
        logger.info("                                                                   ");
        logger.info("=========== Starting CR SYNC Processing. =========================");
        logger.info("                                                                   ");

        jfs = otaqJsdFieldRepository.findAll();

        String authString = otaqValueService.getUrlByCode(USER_NAME) + ":" + otaqValueService.getUrlByCode(USER_PW);
        authStringEnc = new BASE64Encoder().encode(authString.getBytes());
        logger.info("Base64 encoded auth string: " + authStringEnc);

        String infoUrl     = otaqValueService.getUrlByCode(INFO);
        String prjKeyUrl   = otaqValueService.getUrlByCode(PROJECT_KEY_URL);
        String typeUrl     = otaqValueService.getUrlByCode(CR_TYPE);
        String fieldUrl    = otaqValueService.getUrlByCode(FIELD);

        //getInfo(infoUrl, authStringEnc);
         getInfo("fieldUrl", authStringEnc);

        //getInfo(typeUrl, authStringEnc);
        //String CustomFields = getInfo(fieldUrl, authStringEnc);
        //postIssue(issueUrl, authStringEnc);
        //putIssue(issueUrl, authStringEnc);

      String project_key   = otaqValueService.getUrlByCode(PROJECT_KEY);
      try {
            buildJson(project_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalInsert != 0 || totalUpdate !=0 || statusUpdate !=0) {
            logger.info("Insert latest updated date :: " + lastUpdated);
            OtaqProcessStat ps = new OtaqProcessStat();
            ps.setTotalInsert(totalInsert);
            ps.setTotalUpdate(totalUpdate);
            ps.setLastUpdated(lastUpdated);
            ps.setStatusUpdate(statusUpdate);
            ps.setNewRecord(newRecord);
            otaqProcessStatRepository.save(ps);
        }
    }

    private String getInfo(String urlCode, String authStringEnc) {
        logger.info("Rest service Key::: " + urlCode);

        String url = otaqValueService.getUrlByCode(urlCode);

        Client restClient = Client.create();
        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource.accept("application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .get(ClientResponse.class);

        if (resp.getStatus() != 200) {
            logger.error("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            logger.error("Unable to connect to the Atlassian server.");
            logger.error("CR Sync exit.");
            logger.error("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.exit(0);
        }

        String output = resp.getEntity(String.class);
        logger.info("response: " + output);

        JSONObject jsonObject = null;
        JSONArray myResponse = null;
        if(urlCode.equals("prjKeyUrl")) {
            try {
                jsonObject = new JSONObject(output);
                myResponse = jsonObject.getJSONArray("values");
                for (int i = 0; i < myResponse.length(); i++) {
                    logger.info("+++++++++++++++++++++" + myResponse.getJSONObject(i).getString("projectKey"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(urlCode.equals("fieldUrl")) {
            try {
                JSONArray json = new JSONArray(output);
                for(int i=0; i < json.length(); i++) {
                    JSONObject e = json.getJSONObject(i);
                    Iterator keysToCopyIterator = e.keys();
                    while(keysToCopyIterator.hasNext()) {
                        String key = (String) keysToCopyIterator.next();
                        logger.info(i + " " +key+ " " + e.getString(key));
                        for(OtaqJsdField jf : jfs) {
                            if ("name".equals(key) && jf.getJsdFieldName().equals(e.getString(key))) {
                                colkeys.put(convertToCamelcase(jf.getCrFieldName()), e.getString("key"));
                                logger.info(key + " " + jf.getJsdFieldName() + " :: " + e.getString("key") + "  ||" + e.getString("name"));
                            }
                            if("schema".equals(key) && jf.getJsdFieldName().equals(e.getString("name"))) {
                                fieldkeys.put(e.getString("key"), e.getJSONObject("schema").getString("type"));
                                logger.info(key + " " + e.getString("key") + "  ||" + e.getJSONObject("schema").getString("type"));
                            }
                        }
                    }
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        colkeys.entrySet().forEach(entry -> {
            logger.info("col Key : " + entry.getKey() + " Value : " + entry.getValue());
        });

        fieldkeys.entrySet().forEach(entry -> {
            logger.info("filed type : " + entry.getKey() + " Value : " + entry.getValue());
        });


        return output;
    }

    private void postIssue(String url, String authStringEnc, String input) {
        logger.info("++++++++++++++ Insert Request +++++++++++++++++++++++++");
        logger.info("Post service ::: " + url);
        logger.info("Post service json ::: " + input);
        logger.info("                                                                   ");
        Client restClient = Client.create();

        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource.accept("application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .post(ClientResponse.class, input);

        if (resp.getStatus() != 204) {
            String output = resp.getEntity(String.class);
            logger.info("response: " + output);
            if(resp.getStatus() != 400)
                totalInsert++;
        }
        else
            logger.info("Unable to create the JIRA service: " + resp.getStatus() + " " + resp.getStatusInfo());
    }

    private void putIssue(String url, String authStringEnc, String input, String id, String key) {
        // update cr status
        if(id != null) {
                logger.info("++++++++++++++ Update Status +++++++++++++++++++++++++");
                logger.info("Put Rest service ::: " + url + "/" + id);
                logger.info("Put Rest service json " + input);
                logger.info("                                                                   ");
                Client restClient = Client.create();

                WebResource webResource = restClient.resource(url + "/" + id);
                ClientResponse resp = webResource
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic " + authStringEnc)
                        .put(ClientResponse.class, input);

            if (resp.getStatus() != 201) {
                logger.info("Unable to update Service " + id + " due to the status and reason "+ resp.getStatus() + " " + resp.getStatusInfo());
            }
            else
                totalUpdate++;
        }
        // update comment
        else if(key != null){
            logger.info("++++++++++++++ Update Comment +++++++++++++++++++++++++");
            logger.info("Put Rest service ::: " + url + "/" + key + "/comment");
            logger.info("                                                                   ");
            JSONObject main  = new JSONObject();

            Client restClient = Client.create();
            String updatestatus = "{\"body\": \"CR Sync Application updated CR status\"}";
            WebResource webResource = restClient.resource(url + "/" + key + "/comment");
            ClientResponse resp = webResource
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + authStringEnc)
                    .post(ClientResponse.class, updatestatus );

            if (resp.getStatus() != 201) {
                logger.info("Unable to update Comment " + key + " due to the status and reason "+ resp.getStatus() + " " + resp.getStatusInfo());
            }
            else
                statusUpdate++;

        }
    }

    private String isCrcreated(String summary, String url, String authStringEnc) throws JSONException {
        logger.info("Check cr exists ::: " + url+ " Summary >>> "+ summary);
        Client restClient = Client.create();

        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource
                .queryParam("jql", "summary ~ "+"\""+summary+"\"")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .get(ClientResponse.class);

        if (resp.getStatus() != 200) {
            logger.error("Unable to connect to the server" + resp.getStatus() + " " + resp.getStatusInfo());
        }

        String output = resp.getEntity(String.class);
        logger.info("Search response: " + output);

/*
        JSONObject jsonObject = new JSONObject(output);
        JSONArray myResponse = jsonObject.getJSONArray("issues");
        for (int i = 0; i < myResponse.length(); i++) {
            logger.info("+++++++++++++++++++++" + myResponse.getJSONObject(i).getString("id"));
            //
            logger.info("Custom Filed 081 ==> " + myResponse.getJSONObject(i).getJSONObject("fields").getJSONObject("customfield_10081").getString("value"));
        }

        if(myResponse.length() == 1)
            return myResponse.getJSONObject(0).getString("id");
*/

        return output;
    }

    private void buildJson(String projectKey) throws JSONException {
        String issueUrl    = otaqValueService.getUrlByCode(ISSUE);
        String searchUrl   = otaqValueService.getUrlByCode(SEARCH);

        lastUpdated = otaqProcessStatService.getLastUpdated();
        logger.info(" Last updated date ::: " + lastUpdated);

        List<OtaqCustomField> otaqcr = otaqCustomFieldService.getOtaqCustomFieldLastUpdatedDateAfterDesc(lastUpdated);

        if(otaqcr.size() != 0) {
            lastUpdated = otaqCustomFieldService.getLastUpdated(lastUpdated);
            logger.info(" Updated last update date ::: " + lastUpdated);
            newRecord = otaqcr.size();
        }
        else
            logger.info(" +++++++++++++++No new requests created.+++++++++++++++++++");


        for(OtaqCustomField ot : otaqcr){
            logger.info(otaqcr.size() + " " +ot.getSummary() + " " +ot.getCompanyName()+ " "+ot.getLastUpdatedDate());
        }

        ObjectMapper mapper = new ObjectMapper();
        JSONObject input = null;
        String jsonOutString = null;
        String issueid = null;
        String existingCrStatus = null;
        String key = null;
        String response ="";

        for (OtaqCustomField t : otaqcr) {
            //String issueid = isCrcreated(t.getSummary(), searchUrl, authStringEnc);
            response = isCrcreated(t.getSummary(), searchUrl, authStringEnc);
            JSONArray myResponse = null;

            if(response != null) {
                JSONObject jsonObject = new JSONObject(response);
                myResponse = jsonObject.getJSONArray("issues");
                issueid = null;
                key = null;
                existingCrStatus = null;
                if (jsonObject.getInt("total") != 0) {

                    if (jsonObject.getInt("total") > 1)
                        logger.info(" !!! There are more than one to match the search criteria!!!");

                    issueid = myResponse.getJSONObject(0).getString("id");
                    key = myResponse.getJSONObject(0).getString("key");
                    existingCrStatus = myResponse.getJSONObject(0).getJSONObject("fields").getJSONObject(colkeys.get("crStatus")).getString("value");
                }


                logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                logger.info("Key : "+key +" id : "+issueid + " CR Status : "+ existingCrStatus);
                logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }

            String jsonInString = null;
            try {
                jsonInString = mapper.writeValueAsString(t);
                logger.info("JSON String )))) "+ jsonInString);
                input = createJson(jsonInString, projectKey, t.getCrType(), issueid);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < myResponse.length(); i++) {
                logger.info("+++++++++++++++++++++" + myResponse.getJSONObject(i).getString("id"));
                logger.info("crStatus ==> " + myResponse.getJSONObject(i).getJSONObject("fields").getJSONObject("customfield_10081").getString("value"));
            }


           logger.info("After search JIRA issueid = " + issueid);
           logger.info("After search JIRA JSON = " + input.toString());
           if(issueid == null) {
                postIssue(issueUrl, authStringEnc, input.toString());
            }
//            if(existingCrStatus.equals((input.getJSONObject(colkeys.get("crStatus"))).getString("value"))) {
            if(issueid != null && !input.toString().contains(existingCrStatus)) {
               putIssue(issueUrl, authStringEnc, input.toString(), issueid, null);
               //update the comment
               putIssue(issueUrl, authStringEnc, null, null, key);
            }
            else {
                logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                logger.info(issueid + " CR status is not updated:: JIRA " + existingCrStatus + " IS THE SAME :" + input.toString());
                logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }

        }
    }


    private JSONObject createJson(String data, String projectKey, String issueType, String serviceType) {

        logger.info("createJson >>>>>" + serviceType);
        for (String key : colkeys.keySet()) {
            data = data.replace("\""+key+"\":", "\""+colkeys.get(key)+"\":");
            logger.info(key +"####################"+ colkeys.get(key));
        }

        JSONObject fields = null;
        JSONObject main  = new JSONObject();
        JSONObject prj   = new JSONObject();
        JSONObject issue = new JSONObject();
        JSONObject update = new JSONObject();

        try {
            fields = new JSONObject(data);
            prj.put("key", projectKey);
            issue.put("name", issueType);
            fields.put("project", prj);
            fields.put("issuetype", issue);

            logger.info("JSON fields ====> " + fields);
              for (String key : fieldkeys.keySet()) {
                   if("option".equals(fieldkeys.get(key))) {
                        logger.info("option filed " + key);
                        JSONObject opField = new JSONObject();
                        opField.put("value", fields.getString(key));
                        fields.remove(key);
                        fields.put(key, opField);
                        update.put(key, opField);
                    }
              }

              if(serviceType == null) {
                  main.put("fields", fields);
                  logger.info("POST JSON fields ====> " + fields);
              }
              else {
                  main.put("fields", update);
                  logger.info("PUT JSON update ====> " + update);
              }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return main;
    }


    public String convertToCamelcase(String text) {
        String bactrianCamel = Stream.of(text.split("[^a-zA-Z0-9]")) .map(v -> v.substring(0, 1).toUpperCase() +
                v.substring(1).toLowerCase()) .collect(Collectors.joining());
        String dromedaryCamel = bactrianCamel.toLowerCase().substring(0, 1) + bactrianCamel.substring(1);
        logger.info(text + " is ---> "+ dromedaryCamel + "  ===> " +bactrianCamel);

        return dromedaryCamel;
    }
}
