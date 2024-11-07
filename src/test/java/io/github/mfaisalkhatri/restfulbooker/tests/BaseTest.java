package io.github.mfaisalkhatri.restfulbooker.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseTest {

    protected static RequestSpecification requestSpec() {
        final RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        return requestBuilder.addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .setBaseUri("http://localhost:3001").build();
    }

    protected static ResponseSpecification responseSpec() {
        final ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
        return responseBuilder.expectStatusCode(200)
                .expectHeader("Content-Type", "application/json; charset=utf-8")
                .build();

    }


}
