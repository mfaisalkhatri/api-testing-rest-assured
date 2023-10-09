package io.github.mfaisalkhatri;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestfulBookerTests {

    private record UserData(String firstname, String lastname, int totalprice, boolean depositpaid,
                            BookingDates bookingdates, String additionalneeds) {
    }

    private record BookingDates(String checkin, String checkout) {
    }

    private record AuthData(String username, String password) {
    }

    private record PartialUpdateData(int totalprice, String additionalneeds){}

    private static RequestSpecification specBuilder() {
        final RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .setBaseUri("http://localhost:3001/");

        return builder.build();

    }

    @Test
    public void testCreateBooking() {
        final String firstName = "John";
        final UserData userData = new UserData(firstName, "Khatri",
                650, true,
                new BookingDates("2023-10-01", "2023-10-09"),
                "Dinner");


        given().spec(specBuilder())
                .body(userData)
                .when()
                .post("booking")
                .then()
                .statusCode(200)
                .body("bookingid", is(notNullValue()))
                .body("booking.firstname", equalTo(firstName))
                .body("booking.bookingdates.checkin", equalTo("2023-10-01"));
    }

    @Test
    public void testGetBooking() {
        given().spec(specBuilder())
                .when()
                .get("http://localhost:3001/booking/21")
                .then()
                .statusCode(200);

    }

    @Test
    public void testUpdateBooking() {
        final UserData updateUseData = new UserData("Joseph", "Doe", 876, true, new BookingDates("2022-09-01", "2022-09-10"), "Breakfast");

        given().spec(specBuilder())
                .header("Cookie", "token=" + authToken())
                .body(updateUseData)
                .when()
                .put("http://localhost:3001/booking/20")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdatePartialBooking() {
        final String additionalNeeds = "Lunch";
        final PartialUpdateData partialUpdateData = new PartialUpdateData(900, additionalNeeds);

        given().spec(specBuilder())
                .header("Cookie", "token=" + authToken())
                .body(partialUpdateData)
                .when()
                .patch("http://localhost:3001/booking/20")
                .then()
                .statusCode(200)
                .body("additionalneeds", equalTo(additionalNeeds))   ;
    }

    @Test
    public void testDeleteBooking() {
        given().spec(specBuilder())
                .header("Cookie", "token=" + authToken())
                .when()
                .delete("http://localhost:3001/booking/21")
                .then()
                .statusCode(201);
    }

    @Test
    public void testDeletedBooking() {
        given().header("Accept", "application/json")
                .when()
                .get("http://localhost:3001/booking/21")
                .then()
                .statusCode(404);
    }

    public String authToken() {

        final AuthData authData = new AuthData("admin", "password123");

        return given().spec(specBuilder()).body(authData)
                .when()
                .post("http://localhost:3001/auth")
                .then()
                .statusCode(200)
                .body("token", is(notNullValue()))
                .extract().path("token");
    }
}
