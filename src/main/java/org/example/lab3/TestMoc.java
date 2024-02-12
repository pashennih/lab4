package org.example.lab3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestMoc {
    private static final String baseUrl = "https://6c224e9f-6143-4e3d-91c0-da01a7d6ab1e.mock.pstmn.io";
    String token = "PMAK-6556805b7083b2003f4ceab4-3016953f50270dc494dd9fc1b1a8db730b";
    private static final String REQUEST = "/yashchenkoBohdan",
            REQUEST_SUCCESS = REQUEST + "/success",
            REQUEST_UNSUCCESS = REQUEST + "/unsuccess",
            REQUEST_POST200 = REQUEST + "/post200",
            REQUEST_POST400 = REQUEST + "/post400",
            REQUEST_PUT500 = REQUEST + "/put500",
            REQUEST_DELETE = REQUEST + "/delete";


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test()
    public void verifyGetSuccessAction() {
        given().get(REQUEST_SUCCESS)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyGetSuccessAction")
    public void verifyGetUnsuccessAction() {
        given().get(REQUEST_UNSUCCESS)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test(dependsOnMethods = "verifyGetUnsuccessAction")
    public void verifyPost200Action() {
        given().post(REQUEST_POST200)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("result", equalTo("'Nothing' was created"));
    }

    @Test(dependsOnMethods = "verifyPost200Action")
    public void verifyPost400Action() {
        given().post(REQUEST_POST400)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("result", equalTo("You dont have permission to create Something"));
    }

    @Test(dependsOnMethods = "verifyPost400Action")
    public void verifyPut500Action() {
        String name = Faker.instance().name().name();
        String surname = Faker.instance().name().username();
        Map<String, ?> body = Map.of(
                "id", name,
                "name", surname
        );
        given().body(body)
                .put(REQUEST_PUT500)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
    @Test(dependsOnMethods = "verifyPut500Action")
    public void verifyDeleteAction() {
        given()//.pathParam("petId", petId)
                .delete(REQUEST_DELETE)
                .then()
                .statusCode(HttpStatus.SC_GONE)
                .and()
                .body("world", equalTo("0"));
    }
}
