package io.github.mfaisalkhatri.tests;

import io.github.mfaisalkhatri.testdata.AuthData;
import io.github.mfaisalkhatri.testdata.BookingData;
import io.github.mfaisalkhatri.testdata.PartialUpdateBookingData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.github.mfaisalkhatri.testdata.TestDataBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestfulBookerTests extends BaseTest {

    private BookingData bookingData;
    private int bookingId;
    private String token;

    @BeforeClass
    public void setup() {
        this.bookingData = getBookingData();
    }

    @Test
    public void testCreateBooking() {
        this.bookingId = given().spec(requestSpec())
                .body(this.bookingData)
                .when()
                .post("/booking")
                .then()
                .spec(responseSpec())
                .and()
                .assertThat()
                .body("bookingid", is(notNullValue()))
                .body("booking.firstname", equalTo(this.bookingData.firstname()))
                .body("booking.bookingdates.checkin", equalTo(this.bookingData.bookingdates().checkin()))
                .extract()
                .path("bookingid");
    }

    @Test
    public void testGetBooking() {
        given().spec(requestSpec())
                .when()
                .get("/booking/" + this.bookingId)
                .then()
                .spec(responseSpec());
    }

    @Test
    public void testUpdateBooking() {
        final BookingData updateBooking = getBookingData();

        given().spec(requestSpec())
                .header("Cookie", "token=" + this.token)
                .body(updateBooking)
                .when()
                .put("/booking/" + this.bookingId)
                .then()
                .spec(responseSpec());
    }

    @Test
    public void testUpdatePartialBooking() {

        final PartialUpdateBookingData partialUpdateData = getPartialUpdateBookingData();

        given().spec(requestSpec())
                .header("Cookie", "token=" + this.token)
                .body(partialUpdateData)
                .when()
                .patch("/booking/" + this.bookingId)
                .then()
                .spec(responseSpec())
                .body("additionalneeds", equalTo(partialUpdateData.additionalneeds()));
    }

    @Test
    public void testDeleteBooking() {
        given().spec(requestSpec())
                .header("Cookie", "token=" + this.token)
                .when()
                .delete("/booking/" + this.bookingId)
                .then()
                .statusCode(201);
    }

    @Test
    public void testDeletedBooking() {
        given().spec(requestSpec())
                .when()
                .get("/booking/" + this.bookingId)
                .then()
                .statusCode(404);
    }


    @Test
    public void testTokenGeneration() {

        final AuthData authData = getAuthData();

        this.token = given().spec(requestSpec())
                .body(authData)
                .when()
                .post("/auth")
                .then()
                .spec(responseSpec())
                .and()
                .assertThat()
                .body("token", is(notNullValue()))
                .extract().path("token");
    }
}
