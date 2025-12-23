package api_flow;

import builder.BodyJSONBuilder;
import builder.IssueContentBuilder;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.IssueTransition;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueFlow {
    private static Map<String,String> transitionTypeMap = new HashMap<>();
    private static final String issuePathPrefix = "/rest/api/3/issue";
    private RequestSpecification request;
    private String baseURI;
    private Response response;
    private String createdIssueKey;
    private String projectKey;
    private String issueTypeStr;
    private IssueFields issueFields;
    private String status;

    static {
        transitionTypeMap.put("11","To Do");
        transitionTypeMap.put("21","In Progress");
        transitionTypeMap.put("31","Done");
    }

    public IssueFlow(RequestSpecification request,String baseURI,String projectKey,String issueTypeStr) {
        this.request = request;
        this.baseURI = baseURI;
        this.projectKey = projectKey;
        this.issueTypeStr = issueTypeStr;
        this.status = "To Do";
    }

    @Step("Creating Jira Issue")
    public void createIssue(){
        ProjectInfo projectInfo = new ProjectInfo(baseURI,projectKey);
        String taskTypeId = projectInfo.getIssueTypeId(issueTypeStr);

        int desiredLengths = 20;
        boolean hasLetters = true;
        boolean hasNumbers = true;
        String randomSummary = RandomStringUtils.random(desiredLengths,hasLetters,hasNumbers);

        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String issueFieldsContent = issueContentBuilder.build(projectKey,taskTypeId,randomSummary);
        issueFields = issueContentBuilder.getIssueFields();

         this.response = request.body(issueFieldsContent).post(issuePathPrefix);

        Map<String,String> responseBody = JsonPath.from(response.asString()).get();
        createdIssueKey = responseBody.get("key");
    }
    @Step("Verifying Jira Issue")
    public void verifyIssueDetails(){
        Map<String,String> issueInfo = getIssueInfo();
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = status;

        String actualSummary = issueInfo.get("summary");
        String actualStatus = issueInfo.get("status");
        System.out.println("expectedSummary : " + expectedSummary);
        System.out.println("actualSummary : " + actualSummary);
        System.out.println("expectedStatus : " + expectedStatus);
        System.out.println("actualStatus : " + actualStatus);
    }
    @Step("Updating Jira Issue")
    public void updateIssue(String issueStatusStr){
        String targetTransitionId = null;
        for (String transitionId : transitionTypeMap.keySet()) {
            if (transitionTypeMap.get(transitionId).equalsIgnoreCase(issueStatusStr)){
                targetTransitionId = transitionId;
                break;
            }
        }
        if (targetTransitionId == null){
            throw new RuntimeException("[ERR] provided issue status string is not supported");
        }

        String issueTransitionPath = issuePathPrefix + "/"+ createdIssueKey + "/transitions";
        IssueTransition.Transition transition = new IssueTransition.Transition(targetTransitionId);
        IssueTransition issueTransition = new IssueTransition(transition);
        String transitionBody = BodyJSONBuilder.getJSONContent(issueTransition);


        request.body(transitionBody).post(issueTransitionPath).then().statusCode(204);

        Map<String,String> issueInfo = getIssueInfo();
        String actualIssueStatus = issueInfo.get("status");
        String expectedIssueStatus = transitionTypeMap.get(targetTransitionId);
        System.out.println("actualIssueStatus : " + actualIssueStatus);
        System.out.println("expectedIssueStatus : " + expectedIssueStatus);
    }
    @Step("Deleting Jira Issue")
    public void deleteIssue(){
        String path = issuePathPrefix + "/" + createdIssueKey;
        request.delete(path);

        response = request.get(path);
        Map<String, List<String>> noExistingIssueRes = JsonPath.from(response.body().asString()).get();
        List<String> errorMessage =  noExistingIssueRes.get("errorMessages");
        System.out.println("Return error message : " + errorMessage.get(0));
    }
    private Map<String,String> getIssueInfo(){
        String getIssuePath = issuePathPrefix + "/" + createdIssueKey ;
        Response getResponse = request.body("").get(getIssuePath);

        Map<String,Object> fields = JsonPath.from(getResponse.getBody().asString()).get("fields");
        String actualSummary = fields.get("summary").toString();
        Map<String,Object> status = (Map<String,Object>)fields.get("status");
        Map<String,Object> statusCategory = (Map<String,Object>) fields.get("statusCategory");
        String actualStatus = statusCategory.get("name").toString();

        Map<String,String> issueInfo = new HashMap<>();
        issueInfo.put("summary",actualSummary);
        issueInfo.put("status",actualStatus);
        return issueInfo;
    }

}
