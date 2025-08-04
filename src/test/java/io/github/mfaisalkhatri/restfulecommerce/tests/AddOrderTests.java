package io.github.mfaisalkhatri.restfulecommerce.tests;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.github.mfaisalkhatri.restfulecommerce.pojo.Order;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class AddOrderTests {

    @Test
    public void testAddOrder () {
        final List<Order> orders = new ArrayList<> ();
        orders.add (new Order ("1", "34", "Canon Camera CX12", 14500, 1, 915, 15415));
        given ().body (orders)
            .when ()
            .log ()
            .all ()
            .contentType (ContentType.JSON)
            .post ("http://localhost:3004/addOrder")
            .then ()
            .log ()
            .all ()
            .statusCode (201);
    }
}