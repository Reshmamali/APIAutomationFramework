package com.example.tests.crud;

import com.example.base.BaseTest;
import com.example.endpoints.APIConstants;
import com.example.pojos.BookingResponse;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

public class TestCreatBookingPost01 extends BaseTest {

    @Owner("Reshma")
    @Link(name = "Link to tc", url = "https://bugs.atlassion.net/browse")
    @TmsLink("Test management tool link")
    @Issue("EZXR-9192")
    @Description("TC1 Verify create booking POST request")
    @Test
    public void testCreatBookingPost01() {

        requestSpecification
                .basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);

        response = RestAssured
                .given(requestSpecification)
                .when().body(payloadManager.createPayloadBookingAsString()).post();
        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        // Default Rest Assured validation
        validatableResponse.body("booking.firstname", Matchers.equalTo("James"));

        // Deserialization
        BookingResponse bookingResponse = payloadManager.bookingResponseJava(response.asString());

        // AssertJ
        assertThat(bookingResponse.getBookingid()).isNotNull();
        assertThat(bookingResponse.getBooking().getFirstname()).isNotNull().isEqualTo("James");
        assertThat(bookingResponse.getBooking().getLastname()).isNotNull().isEqualTo("Brown");

        //TestNG Assertion
        assertActions.verifyStatusCode(response, 200);
        assertActions.verifyResponseBody(bookingResponse.getBooking().getFirstname(),"James", "Verify firstname");
        assertActions.verifyResponseBody(bookingResponse.getBooking().getLastname(),"Brown", "Verify lastname");

    }


}
