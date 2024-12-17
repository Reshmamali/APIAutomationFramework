package com.example.tests.crud;

import com.example.base.BaseTest;
import com.example.endpoints.APIConstants;
import com.example.pojos.Booking;
import com.example.pojos.BookingResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestIntegrationFlow extends BaseTest {
    // Create A Booking, Create a Token
    // Get booking
    // Update the Booking
    // Delete the Booking


    @Test(groups = "integration", priority = 1)
    @Owner("Promode")
    @Description("TC#INT1 - Step 1. Verify that the Booking can be Created")
    public void testCreateBooking(ITestContext iTestContext){
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
        response = RestAssured.given(requestSpecification)
                .when().body(payloadManager.createPayloadBookingAsString())
                .post();
        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        BookingResponse bookingResponse = payloadManager.bookingResponseJava(response.asString());
        iTestContext.setAttribute("bookingid",bookingResponse.getBookingid());
    }

    @Test(groups = "integration", priority = 2)
    @Owner("Promode")
    @Description("TC#INT1 - Step 2. Verify that the Booking By ID")
    public void testVerifyBookingId(ITestContext iTestContext){
        Integer bookingId = (Integer)iTestContext.getAttribute("bookingid");

        String basePathGet = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingId;

        requestSpecification.basePath(basePathGet);
        response = RestAssured.given(requestSpecification).when().log().all().get();
        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        BookingResponse bookingResponse = payloadManager.bookingResponseJava(response.asString());
        assertThat(bookingId).isNotZero().isNotNull().isNotNegative();
    }

    @Test(groups = "integration", priority = 3)
    @Owner("Promode")
    @Description("TC#INT1 - Step 3. Verify Updated Booking by ID")
    public void testUpdateBookingByID(ITestContext iTestContext){
        String token = getToken();
        iTestContext.setAttribute("token",token);
        Integer bookingid = (Integer)iTestContext.getAttribute("bookingid");

        String basePathPutPatch = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid ;

        requestSpecification.basePath(basePathPutPatch);
        response = RestAssured
                .given(requestSpecification).body(payloadManager.fullUpdatePayloadAsString())
                .cookie("token",token)
                .when().log().all().put();

        validatableResponse = response.then().log().all();
        // Validatable assertions
        validatableResponse.statusCode(200);

        Booking booking = payloadManager.getResponseFromJSON(response.asString());
        assertThat(booking.getFirstname()).isEqualTo("Reshma");
        assertThat(booking.getLastname()).isEqualTo("Mali");
    }

    @Test(groups = "integration", priority = 4)
    @Owner("Promode")
    @Description("TC#INT1 - Step 4. Delete the Booking by ID")
    public void testDeleteBookingById(ITestContext iTestContext){
        String token = (String)iTestContext.getAttribute("token");
        Integer bookingid =(Integer)iTestContext.getAttribute("bookingid");


       String basPathDelete = APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingid ;

       requestSpecification.basePath(basPathDelete);
       response = RestAssured
               .given(requestSpecification).cookie("token",token)
               .when().log().all().delete();
       validatableResponse = response.then().log().all();
       validatableResponse.statusCode(201);

    }
}
