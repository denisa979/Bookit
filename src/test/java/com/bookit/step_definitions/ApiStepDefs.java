package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DB_Util;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {

    String token;
    Response response;
    String emailGlobal;

    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {
        token = BookitUtils.generateTokenByRole(role);
        System.out.println("token = " + token);

        Map<String, String> credentialsMap = BookitUtils.returnCredentials(role);

        emailGlobal = credentialsMap.get("email");


    }

    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {
         response = given().accept(ContentType.JSON)
                .header("Authorization", token)
                .when().get(Environment.BASE_URL + endpoint);
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        System.out.println("response.statusCode() = " + response.statusCode());
        //verify status code
        Assert.assertEquals(expectedStatusCode,response.statusCode());

    }

    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {
        System.out.println("response.contentType() = " + response.contentType());
        //verify content type
        Assert.assertEquals(expectedContentType,response.contentType());
    }
    @Then("role is {string}")
    public void role_is(String expectedRole) {
        response.prettyPrint();
        String actualRole = response.path("role");

        Assert.assertEquals(expectedRole,actualRole);
    }

    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {
        response.prettyPrint();
        //GET DATA FROM API
        JsonPath jsonPath = response.jsonPath();
        /*
        {
            "id": 17381,
            "firstName": "Raymond",
            "lastName": "Reddington",
            "role": "student-team-member"
        }
         */
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //GET DATA FROM DATABASE
        //first we need to create database connection which will handle by custom hooks
        String query = "select firstname,lastname,role from users\n" +
                "where email ='"+emailGlobal+"'";
        //run your query
        DB_Util.runQuery(query);

        //get the result to map
        Map<String, String> dbMap = DB_Util.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String expectedFirstName = dbMap.get("firstname");
        String expectedLastName = dbMap.get("lastname");
        String expectedRole = dbMap.get("role");

        //COMPARE API vs DB

        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);

    }

    @Then("UI,API and Database user information must be match")
    public void ui_api_and_database_user_information_must_be_match() {
        response.prettyPrint();
        //GET DATA FROM API
        JsonPath jsonPath = response.jsonPath();
        /*
        {
            "id": 17381,
            "firstName": "Raymond",
            "lastName": "Reddington",
            "role": "student-team-member"
        }
         */
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //GET DATA FROM DATABASE
        //first we need to create database connection which will handle by custom hooks
        String query = "select firstname,lastname,role from users\n" +
                "where email ='"+emailGlobal+"'";
        //run your query
        DB_Util.runQuery(query);

        //get the result to map
        Map<String, String> dbMap = DB_Util.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String expectedFirstName = dbMap.get("firstname");
        String expectedLastName = dbMap.get("lastname");
        String expectedRole = dbMap.get("role");

        //COMPARE API vs DB

        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);

        //GET DAT FROM UI
        SelfPage selfPage = new SelfPage();

        String actualFullNameUI = selfPage.name.getText();
        String actualRoleUI = selfPage.role.getText();
        System.out.println("actualFullNameUI = " + actualFullNameUI);
        //UI vs DB
        String expectedFullName = expectedFirstName+" "+expectedLastName;

        Assert.assertEquals(expectedFullName,actualFullNameUI);
        Assert.assertEquals(expectedRole,actualRoleUI);


        //UI vs API
        String expectedNameFromAPI = actualFirstName+" "+actualLastName;
        Assert.assertEquals(expectedNameFromAPI,actualFullNameUI);
        Assert.assertEquals(actualRole,actualRoleUI);
    }

    //ADDING A NEW STUDENT AND DELETING IT

    @When("I send POST request {string} endpoint with following information")
    public void i_send_post_request_endpoint_with_following_information(String endpoint, Map<String,String> studentInfo) {
        response=given().accept(ContentType.JSON)
                        .header("Authorization",token)
                        .queryParams(studentInfo)
                .when().post(Environment.BASE_URL +endpoint).prettyPeek();

    }
    @Then("I delete previously added student")
    public void i_delete_previously_added_student() {
        //we need to get the entryiId from the post request and send delete request to it.
        int idToDelete = response.path("entryiId");
        System.out.println("idToDelete = " + idToDelete);

        //Send DELETE request to idToDelete path parameter
        given()
                .header("Authorization",token)
                .pathParam("id",idToDelete)
        .when()
                .delete(Environment.BASE_URL+"/api/students/{id}")
                .then().statusCode(204);

    }


}
