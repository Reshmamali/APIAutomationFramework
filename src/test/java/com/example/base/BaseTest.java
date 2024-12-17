package com.example.base;

import com.example.asserts.AssertActions;
import com.example.endpoints.APIConstants;
import com.example.modules.PayloadManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    public RequestSpecification requestSpecification;
    public Response response;
    public ValidatableResponse validatableResponse;
    public AssertActions assertActions;
    public PayloadManager payloadManager;
    public JsonPath jsonPath;


    @BeforeTest
    public void setUp(){
        // BASE URL. Content Type JSON will remain same for all the test cases
        payloadManager = new PayloadManager();
        assertActions = new AssertActions();

        requestSpecification =
                RestAssured.given()
                        .baseUri(APIConstants.BASE_URL)
                        .contentType(ContentType.JSON)
                        .log().all();

    }

    public String getToken(){
        requestSpecification
                .baseUri(APIConstants.BASE_URL)
                .basePath(APIConstants.AUTH_URL);

//        Setting up the payload
        String payload = payloadManager.setAuthPayload();

//        Get the token
        response = RestAssured
                .given(requestSpecification).contentType(ContentType.JSON).body(payload).when().log().all().post();

//        String extraction
        String token = payloadManager.getTokenFromJSON(response.asString());
        return token;
    }

}
