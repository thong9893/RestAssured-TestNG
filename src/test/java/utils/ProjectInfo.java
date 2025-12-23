package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.ENV;
import model.RequestCapability;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProjectInfo implements RequestCapability {
    private String baseUri;
    private String projectKey;
    private List<Map<String,String>> issueTypes;
    Map<String,List<Map<String,String>>> projectInfo;

    public ProjectInfo(String baseUri, String projectKey) {
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        getProjectInfo();
    }
    public String getIssueTypeId(String issueTypeStr){
        getIssueTypes();
        String issueTypeId = null;

        for (Map<String,String> issueType: issueTypes ) {
            if(issueTypeStr.equalsIgnoreCase(issueType.get("name"))){
                issueTypeId = issueType.get("id");
                break;
            }
        }

        if (issueTypeId == null){
            throw new RuntimeException("[ERROR] can't find id for " + issueTypeStr);
        }
        return issueTypeId;
    }
    private void getIssueTypes(){
         issueTypes = projectInfo.get("issueTypes");
    }
    private void getProjectInfo(){
        String path = "/rest/api/3/project/".concat(projectKey);

        String encodeStr = AuthenticationHandler.encodedCredStr(ENV.email,ENV.token);

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(getAuthenticatedHeader.apply(encodeStr));

        Response response = request.get(path);
        projectInfo = JsonPath.from(response.asString()).get();
    }
}
