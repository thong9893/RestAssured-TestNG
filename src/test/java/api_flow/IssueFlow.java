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
import org.testng.Assert; // Import TestNG Assert
import utils.ProjectInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueFlow {
    private static Map<String, String> transitionTypeMap = new HashMap<>();
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
        transitionTypeMap.put("11", "To Do");
        transitionTypeMap.put("21", "In Progress");
        transitionTypeMap.put("31", "Done");
    }

    public IssueFlow(RequestSpecification request, String baseURI, String projectKey, String issueTypeStr) {
        this.request = request;
        this.baseURI = baseURI;
        this.projectKey = projectKey;
        this.issueTypeStr = issueTypeStr;
        this.status = "To Do";
    }

    @Step("Creating Jira Issue")
    public void createIssue() {
        ProjectInfo projectInfo = new ProjectInfo(baseURI, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId(issueTypeStr);

        int desiredLengths = 20;
        String randomSummary = RandomStringUtils.random(desiredLengths, true, true);

        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String issueFieldsContent = issueContentBuilder.build(projectKey, taskTypeId, randomSummary);
        issueFields = issueContentBuilder.getIssueFields();

        this.response = request.body(issueFieldsContent).post(issuePathPrefix);


        Assert.assertEquals(response.getStatusCode(), 201, "Failed to create Jira issue!");

        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        createdIssueKey = responseBody.get("key");
        Assert.assertNotNull(createdIssueKey, "Issue Key should not be null");
    }

    @Step("Verifying Jira Issue")
    public void verifyIssueDetails() {
        Map<String, String> issueInfo = getIssueInfo();
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = status;

        String actualSummary = issueInfo.get("summary");
        String actualStatus = issueInfo.get("status");


        Assert.assertEquals(actualSummary, expectedSummary, "Summary does not match!");
        Assert.assertEquals(actualStatus, expectedStatus, "Status does not match!");
    }

    @Step("Updating Jira Issue to {0}")
    public void updateIssue(String issueStatusStr) {
        String targetTransitionId = null;
        for (String transitionId : transitionTypeMap.keySet()) {
            if (transitionTypeMap.get(transitionId).equalsIgnoreCase(issueStatusStr)) {
                targetTransitionId = transitionId;
                break;
            }
        }

        Assert.assertNotNull(targetTransitionId, "[ERR] Provided issue status '" + issueStatusStr + "' is not supported");

        String issueTransitionPath = issuePathPrefix + "/" + createdIssueKey + "/transitions";
        IssueTransition.Transition transition = new IssueTransition.Transition(targetTransitionId);
        IssueTransition issueTransition = new IssueTransition(transition);
        String transitionBody = BodyJSONBuilder.getJSONContent(issueTransition);

        // Verify status code 204 (No Content) sau khi update transition thành công
        request.body(transitionBody).post(issueTransitionPath).then().statusCode(204);

        Map<String, String> issueInfo = getIssueInfo();
        String actualIssueStatus = issueInfo.get("status");
        String expectedIssueStatus = transitionTypeMap.get(targetTransitionId);

        Assert.assertEquals(actualIssueStatus, expectedIssueStatus, "Updated status does not match!");
    }

    @Step("Deleting Jira Issue")
    public void deleteIssue() {
        String path = issuePathPrefix + "/" + createdIssueKey;


        Response deleteResponse = request.delete(path);
        Assert.assertEquals(deleteResponse.getStatusCode(), 204, "Failed to delete issue!");


        response = request.get(path);
        Assert.assertEquals(response.getStatusCode(), 404, "Issue still exists after deletion!");

        Map<String, List<String>> noExistingIssueRes = JsonPath.from(response.body().asString()).get();
        List<String> errorMessages = noExistingIssueRes.get("errorMessages");

        Assert.assertTrue(errorMessages.size() > 0, "Error messages list should not be empty");
        Assert.assertEquals(errorMessages.get(0), "Issue does not exist or you do not have permission to see it.", "Error message mismatch!");
    }

    private Map<String, String> getIssueInfo() {
        String getIssuePath = issuePathPrefix + "/" + createdIssueKey;
        Response getResponse = request.body("").get(getIssuePath);

        Assert.assertEquals(getResponse.getStatusCode(), 200, "Failed to get issue details!");

        Map<String, Object> fields = JsonPath.from(getResponse.getBody().asString()).get("fields");
        String actualSummary = fields.get("summary").toString();


        Map<String, Object> statusObj = (Map<String, Object>) fields.get("status");
        String actualStatus = statusObj.get("name").toString();

        Map<String, String> issueInfo = new HashMap<>();
        issueInfo.put("summary", actualSummary);
        issueInfo.put("status", actualStatus);
        return issueInfo;
    }
}