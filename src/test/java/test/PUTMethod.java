package test;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static model.RequestCapability.defaultHeader;
import static org.hamcrest.Matchers.equalTo;

public class PUTMethod {
    public static void main(String[] args) {
        String baseURI = "https://jsonplaceholder.typicode.com";

        RequestSpecification request = given();
        request.baseUri(baseURI);
        request.header(defaultHeader);

        PostBody postBody1 = new PostBody(1,1,"New Title 1","New Body 1");
        PostBody postBody2 = new PostBody(1,1,"New Title 2","New Body 2");
        PostBody postBody3 = new PostBody(1,1,"New Title 3","New Body 3");
        List<PostBody> postBodies = Arrays.asList(postBody1,postBody2,postBody3);

        for (PostBody postBody: postBodies) {
            Gson gson = new Gson();
            String postBodyString = gson.toJson(postBody);

            final int TARGET_POST_NUM = 1;
            Response response = request.body(postBodyString).put("/posts/".concat(String.valueOf(TARGET_POST_NUM)));
            response.prettyPrint();
            response.then().body("title",equalTo(postBody.getTitle()));
            response.then().body("body",equalTo(postBody.getBody()));
        }
    }
}
