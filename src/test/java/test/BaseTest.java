package test;

import io.restassured.specification.RequestSpecification;
import model.ENV;
import model.RequestCapability;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.AuthenticationHandler;

import static io.restassured.RestAssured.given;

public class BaseTest implements RequestCapability {
    protected String encodedCredStr ;
    protected String baseUri ;
    protected String projectKey ;
    protected RequestSpecification request;

    @BeforeSuite
    public void beforeSuite(){
        encodedCredStr = AuthenticationHandler.encodedCredStr(ENV.email,ENV.token);
        baseUri = "https://thongluminh.atlassian.net";
        projectKey = "RES";
    }
    @BeforeTest
    public void beforeTest(){
        request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCredStr));
    }
}
