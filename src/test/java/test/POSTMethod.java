package test;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import static io.restassured.RestAssured.given;
import static model.RequestCapability.defaultHeader;

public class POSTMethod {
    public static void main(String[] args) {
        String baseURI = "https://jsonplaceholder.typicode.com";

        RequestSpecification request = given();
        request.baseUri(baseURI);
        request.header(defaultHeader);

        Gson gson = new Gson();
        PostBody postBody = new PostBody();
        postBody.setUserId(1);
        postBody.setId(1);
        postBody.setTitle("the re's title");
        postBody.setBody("the re's body");


        Response response = request.body(gson.toJson(postBody)).post("/posts");
        response.prettyPrint();
    }
}
