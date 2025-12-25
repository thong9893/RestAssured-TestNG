package test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.ENV;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SimpleTest {
    public static void main(String[] args) {
        String baseURI = "https://jsonplaceholder.typicode.com";

        RequestSpecification request = given();
        request.baseUri(baseURI);
        request.basePath("/todos");

        final String FIRST_TODO = "/1";
        Response response = request.get(FIRST_TODO);
        response.prettyPrint();
        response.then().body("userId",equalTo(2));
        response.then().body("id",equalTo(1));
        response.then().body("title",equalTo("delectus aut autem"));
        response.then().body("completed",equalTo(false));



    }
}
