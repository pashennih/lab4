package org.example.lab2;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class IndividualTest {
    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String PET = "/pet",
            PET_ID = PET + "/{petId}";
    private int petId = 111;
    private String name = Faker.instance().name().name();;
    private String changedName;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test()
    public void verifyCreateAction() {
        Map<String, ?> body = Map.of(
                "id", petId,
                "name", name,
                "status", "available"
        );
        given().body(body)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyCreateAction")
    public void verifyGetAction() {
        given().pathParam("petId", petId)
                .get(PET_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("name", equalTo(name));
    }
    @Test(dependsOnMethods = "verifyGetAction")
    public void verifyChangeAction() {
        changedName = Faker.instance().name().name();
        Map<String, ?> body = Map.of(
                "id", petId,
                "name", changedName,
                "status", "available"
        );
        given().body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyChangeAction")
    public void verifyPutAction() {
        given().pathParam("petId", petId)
                .get(PET_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("name", equalTo(changedName));
    }

    @Test(dependsOnMethods = "verifyPutAction")
    public void verifyDeleteAction() {
        given().pathParam("petId", petId)
                .delete(PET_ID)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }



}
