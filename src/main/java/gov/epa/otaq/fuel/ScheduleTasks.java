package gov.epa.otaq.fuel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import gov.epa.otaq.fuel.model.OtaqJsdField;
import gov.epa.otaq.fuel.model.OtaqRegRequest;
import gov.epa.otaq.fuel.service.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JYue on 1/7/2018.
 */
@Component
public class ScheduleTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    static private String USER_NAME = "name";
    static private String USER_PW   = "password";
    static private String SEARCH    = "searchUrl";
    static private String INFO      = "infoUrl";
    static private String PROJECT_KEY    = "prjKeyUrl";
    static private String CR_TYPE    = "typeUrl";
    static private String FIELD    = "fieldUrl";
    static private String ISSUE    = "issueUrl";

    String searchUrl;
    String infoUrl;
    String prjKeyUrl;
    String typeUrl;
    String fieldUrl;
    String issueUrl;

    HashMap<String, String> colkeys = new HashMap();
    HashMap<String, String> fieldkeys = new HashMap();
    HashMap<String, String> optionkeys = new HashMap();
    protected long dateTime;
    protected Date lastUpdated;
    protected GregorianCalendar calendar;
    protected Timestamp timestamp;
    private List<OtaqJsdField> jfs;
    private Date lastProcessed;
    private String authStringEnc;

    @Autowired
    private OtaqRegRequestRepository otaqRegRequestRepository;

    @Autowired
    private OtaqJsdFieldRepository otaqJsdFieldRepository;

    @Autowired
    OtaqRegRequestService otaqRegRequestService;

    @Autowired
    OtaqValueRepository otaqValueRepository;

    @Autowired
    OtaqValueService otaqValueService;

    @Autowired
    OtaqProcessStatRepository otaqProcessStatRepository;

    @Autowired
    OtaqProcessStatService otaqProcessStatService;

    //@Scheduled(fixedRate = 20000000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

        jfs = otaqJsdFieldRepository.findAll();

        String authString = otaqValueService.getUrlByCode(USER_NAME) + ":" + otaqValueService.getUrlByCode(USER_PW);
        authStringEnc = new BASE64Encoder().encode(authString.getBytes());
        logger.info("Base64 encoded auth string: " + authStringEnc);

        searchUrl   = otaqValueService.getUrlByCode(SEARCH);
        infoUrl     = otaqValueService.getUrlByCode(INFO);
        prjKeyUrl   = otaqValueService.getUrlByCode(PROJECT_KEY);
        typeUrl     = otaqValueService.getUrlByCode(CR_TYPE);
        fieldUrl    = otaqValueService.getUrlByCode(FIELD);
        issueUrl    = otaqValueService.getUrlByCode(ISSUE);

        //getInfo(infoUrl, authStringEnc);
         getInfo("fieldUrl", authStringEnc);

        //getInfo(typeUrl, authStringEnc);
        //String CustomFields = getInfo(fieldUrl, authStringEnc);
        //postIssue(issueUrl, authStringEnc);
        //putIssue(issueUrl, authStringEnc);

      try {
            buildJson("SAMPLE", "Demo CR Issue");
        } catch (JSONException e) {
            e.printStackTrace();
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
            logger.error("Unable to connect to the server");
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
            System.out.println("col Key : " + entry.getKey() + " Value : " + entry.getValue());
        });

        fieldkeys.entrySet().forEach(entry -> {
            System.out.println("filed type : " + entry.getKey() + " Value : " + entry.getValue());
        });


        return output;
    }

    private void postIssue(String url, String authStringEnc) {
        logger.info("Post service ::: " + url);
        Client restClient = Client.create();
 /*       String input = "{" +
                "    \"fields\": {" +
                "       \"project\":" +
                "       { " +
                "          \"key\": \"SAMPLE\"" +
                "       }," +
                "       \"summary\": \"Company Association Request (38068) for Alon Refining Krotz Springs, Inc.\"," +
                "       \"issuetype\": {" +
                "          \"name\": \"Demo CR Issue\"" +
                "       }," +
                "       \"customfield_10091\": 38068," +
                "       \"customfield_10092\": \"CR-17106\"," +
                "       \"customfield_10096\": 3189," +
                "       \"customfield_10095\": \"LISAHALL\"," +
                "       \"customfield_10085\": \"Lisa Hall\"," +
                "       \"customfield_10086\": \"lisa.hall@delekus.com\"," +
                "       \"customfield_10094\": 6464," +
                "       \"customfield_10071\": \"Alon Refining Krotz Springs, Inc.\"," +
                "       \"customfield_10074\": { \"value\": \"Company Association\" }," +
                "       \"customfield_10081\": { \"value\": \"Awaiting RCO Wet Ink Signature\" }," +
                "       \"customfield_10087\": \"2017-10-04T08:28:58.000-0600\"," +
                "       \"customfield_10088\": \"2017-10-04T08:28:58.000-0600\"" +
                "   }" +
                "}";
 */       String input = "{\"fields\":{\"summary\":\"This is summary for test.\",\"issuetype\":{\"name\":\"Demo CR Issue\"},\"customfield_10091\":39090,\"customfield_10092\":\"CR-17471\",\"customfield_10081\":{\"value\":\"Awaiting EPA Review\"},\"customfield_10071\":\"MELISSA RENEWABLES, LLC\",\"customfield_10094\":null,\"customfield_10095\":\"SBDUNPHY2\",\"customfield_10096\":3482,\"customfield_10085\":\"Sandra B. Dunphy\",\"customfield_10074\":{\"value\":\"New Company\"},\"customfield_10086\":\"sandra.dunphy@weaver.com\",\"project\":{\"key\":\"SAMPLE\"},\"customfield_10087\":\"2017-12-19T05:00:00.000Z\",\"customfield_10088\":\"2018-01-09T05:00:00.000Z\"}}";



        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource.accept("application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .post(ClientResponse.class, input);

        logger.info(input);
        if (resp.getStatus() != 200) {
            logger.error("Unable to connect to the server" + resp.getStatus() + " " + resp.getStatusInfo());
        }

        String output = resp.getEntity(String.class);
        logger.info("response: " + output);
    }

    private void putIssue(String url, String authStringEnc) {
        logger.info("Put Rest service ::: " + url+"/"+10049);
        Client restClient = Client.create();

        String input = "{" +
                "    \"fields\": {" +
                "       \"customfield_10081\": { \"value\": \"Awaiting RCO Wet Ink Signature\"}, " +
                "       \"customfield_10074\": { \"value\": \"RCO Update\" }" +
                "   }" +
                "}";

        WebResource webResource = restClient.resource(url+"/"+10049);
        ClientResponse resp = webResource
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .put(ClientResponse.class, input);

        logger.info(input);
        if (resp.getStatus() != 200) {
            logger.error("Unable to connect to the server" + resp.getStatus() + " " + resp.getStatusInfo());
        }
    }

    private void postIssue(String url, String authStringEnc, String input) {
        logger.info("Post service ::: " + url);
        logger.info("Post service json ::: " + input);
        Client restClient = Client.create();

        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource.accept("application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .post(ClientResponse.class, input);

        if (resp.getStatus() != 200) {
            logger.error("Unable to connect to the server " + resp.getStatus() + " " + resp.getStatusInfo());
        }

        if (resp.getStatus() != 204) {
            String output = resp.getEntity(String.class);
            logger.info("response: " + output);
        }
    }

    private void putIssue(String url, String authStringEnc, String input, String id) {
        logger.info("Put Rest service ::: " + url+"/"+id);
        logger.info("Put Rest service json " + input);
        Client restClient = Client.create();

        WebResource webResource = restClient.resource(url+"/"+id);
        ClientResponse resp = webResource
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .put(ClientResponse.class, input);

        if (resp.getStatus() != 200) {
            logger.error("Unable to connect to the server " + resp.getStatus() + " " + resp.getStatusInfo());
        }
    }

    private String isCrcreated(long id, String url, String authStringEnc) throws JSONException {
        logger.info("Check cr exists ::: " + url);
        Client restClient = Client.create();

        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource
                .queryParam("jql", "'CR ID'="+id)
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .get(ClientResponse.class);

        if (resp.getStatus() != 200) {
            logger.error("Unable to connect to the server" + resp.getStatus() + " " + resp.getStatusInfo());
        }

        String output = resp.getEntity(String.class);
        logger.info("Search response: " + output);

        JSONObject jsonObject = new JSONObject(output);
        JSONArray myResponse = jsonObject.getJSONArray("issues");
        for (int i = 0; i < myResponse.length(); i++) {
            logger.info("+++++++++++++++++++++" + myResponse.getJSONObject(i).getString("id"));
        }

        if(myResponse.length() == 1)
            return myResponse.getJSONObject(0).getString("id");

        return null;
    }

    private void buildJson(String projectKey, String issueType) throws JSONException {
        lastUpdated = otaqProcessStatService.getLastUpdated();

        List<OtaqRegRequest> otaqcr = otaqRegRequestService.getOtaqRegRequestsLastUpdatedAfter(lastUpdated);

        for(OtaqRegRequest ot : otaqcr){
            System.out.println(otaqcr.size() + " " +ot.getCrId() + " " +ot.getCompanyName()+ " "+ot.getLastUpdated());
        }
        logger.info(lastUpdated.toString());

        ObjectMapper mapper = new ObjectMapper();
        JSONObject input = null;
        String jsonOutString = null;

        for (OtaqRegRequest t : otaqcr) {
            String issueid = isCrcreated(t.getCrId(), searchUrl, authStringEnc);
            String jsonInString = null;
            try {
                jsonInString = mapper.writeValueAsString(t);
                logger.info("JSON String )))) "+ jsonInString);
                input = createJson(jsonInString, projectKey, issueType, issueid);
                //jsonOutString = mapper.writeValueAsString(input);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
           logger.info("After search JIRA issueid = " + issueid);
           logger.info("After search JIRA JSON = " + input.toString());
           if(issueid == null) {
                postIssue(issueUrl, authStringEnc, input.toString());
            }
            else {
               putIssue(issueUrl, authStringEnc, input.toString(), issueid);
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

                  fields.put("summary", "This is summary for test.");
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

    public void scheduleTaskWithFixedDelay() {}

    public void scheduleTaskWithInitialDelay() {}

    public void scheduleTaskWithCronExpression() {}

    public String convertToCamelcase(String text) {
        String bactrianCamel = Stream.of(text.split("[^a-zA-Z0-9]")) .map(v -> v.substring(0, 1).toUpperCase() +
                v.substring(1).toLowerCase()) .collect(Collectors.joining());
        String dromedaryCamel = bactrianCamel.toLowerCase().substring(0, 1) + bactrianCamel.substring(1);
        logger.info(text + " is ---> "+ dromedaryCamel + "  ===> " +bactrianCamel);

        return dromedaryCamel;
    }
}
