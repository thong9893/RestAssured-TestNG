package model;

import io.restassured.http.Header;

import java.util.function.Function;

public interface RequestCapability {
    Header defaultHeader = new Header("Content-type","application/json; charset=UTF-8");
    Header acceptJSONHeader = new Header("Accept","application/json");
    Function<String, Header> getAuthenticatedHeader = encodeCredStr -> {
        if (encodeCredStr == null){
            throw new IllegalArgumentException("[ERROR] encodeCredStr can't be null");
        }
        return  new Header("Authorization","Basic ".concat(encodeCredStr));
    };
}
