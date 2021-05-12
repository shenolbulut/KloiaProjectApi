package com.kloia.Tasks;

import com.kloia.pojos.Pets;
import static org.hamcrest.Matchers.*;
import com.kloia.utilities.JsonUtil;
import com.kloia.utilities.ObjectUtil;
import com.kloia.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Task1
 * Posting Json data and validating by getting response body
 */
public class Task01 extends TestBase {

    /**
     * Creating java format data by pojos
     */
    Pets pet= ObjectUtil.createJavaFormatDataByPojos();

    /**
     * Creating post request
     */
    @Test(priority = 1)
    public void postRequest() {


        /**
         * Converting java object to json format String
         */
        String jsonFormatString=JsonUtil.convertJavaToJson(pet);


        /**
         * Creating Post Request to "https://petstore.swagger.io/v2/pet"
         */
         response= given().
                        contentType(ContentType.JSON).
                        spec(requestSpecification01).
                        body(jsonFormatString).
                    when().
                        post("/pet");

         response.then().statusCode(200)
                        .contentType("application/json");

        /**
         * initializing id number in to the pet object by using setter method
         */
         long x=response.path("id");
         pet.setId(x);

    }



    /**
     * Verify the below requested in the response
     * -Status code should be 200
     * -Response should has an id
     * -Created name should be equal to the posted name
     * -Content-type should be application/json
     * -Response header should has a date value
     */
    @Test(priority = 2)
    public void getRequest()
    {

        /**
         * creating get request
         */
        Response response=given().
                accept(ContentType.JSON).
                pathParam("id",pet.getId()).
                when().
                get("https://petstore.swagger.io/v2/pet/{id}");

        /**
         * assertions
         */

        response.
                then().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", equalTo(pet.getName())).
                and().header("Date", notNullValue());

    }

    @Test(priority = 3)
    public void deletePet(){


        Response response=given().accept(ContentType.JSON).
                        and().
                        pathParam("id",pet.getId()).
                        when().
                        delete("https://petstore.swagger.io/v2/pet/{id}");

        response.then().
                assertThat().
                statusCode(200).
                body("message",equalTo(pet.getId()),
                        "type",equalTo("unknown"));

    }
}
