package io.github.mfaisalkhatri.restfulecommerce.tests;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.github.mfaisalkhatri.restfulecommerce.pojo.Order;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class AddOrderTests {

    @Test
    @Severity (SeverityLevel.NORMAL)
    @Description ("Create Order test with POST /addOrder API")
    public void testAddOrder () {
        final List<Order> order = createOrderPayload ();
        sendOrderRequest (order);
    }

    @Step ("Create order payload")
    public List<Order> createOrderPayload () {
        final List<Order> orders = new ArrayList<> ();
        orders.add (new Order ("1", "34", "Canon Camera CX12", 14500, 1, 915, 15415));
        return orders;
    }

    @Step ("Send POST order request and verify the response")
    public void sendOrderRequest (final List<Order> orders) {
        given ().when ()
            .filter (new AllureRestAssured ())
            .body (orders)
            .contentType (ContentType.JSON)
            .post ("http://localhost:3004/addOrder")
            .then ()
            .statusCode (201);
    }
}