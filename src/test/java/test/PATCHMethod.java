package test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.BuildModelJSON;
import model.PostBody;
import model.RequestCapability;

import static io.restassured.RestAssured.given;

public class PATCHMethod implements RequestCapability {
    public static void main(String[] args) {
        String baseURI = "https://jsonplaceholder.typicode.com";

        RequestSpecification request = given();
        request.baseUri(baseURI);
        request.header(defaultHeader);

        PostBody postBody = new PostBody();
        postBody.setTitle("New Title 1");

        String postBodyStr = BuildModelJSON.parseJSONString(postBody);

        final String TARGET_POST_ID = "1";
        Response response = request.body(postBodyStr).patch("/posts/".concat(TARGET_POST_ID));
        response.prettyPrint();
    }


}
