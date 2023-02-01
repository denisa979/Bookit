package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookitUtils {

    public static String generateToken(String email, String password) {

        Response response = given()
                .accept(ContentType.JSON)
                .queryParam("email", email)
                .and()
                .queryParam("password", password)
                .when().get(ConfigurationReader.getProperty("base_url") + "/sign");

        String token = "Bearer " + response.path("accessToken");

        return token;
    }


    public static String generateTokenByRole(String role) {

        //returnCredentials(role);


        String token = given()
                .queryParams(returnCredentials(role))
                .when().get(ConfigurationReader.getProperty("base_url") + "/sign").prettyPeek().path("accessToken");

        return "Bearer " + token;

    }

    public static Map<String, String> returnCredentials(String role) {
        String email = "";
        String password = "";

        switch (role) {
            case "teacher":
                email = ConfigurationReader.getProperty("teacher_email") ;
                password = ConfigurationReader.getProperty("teacher_password") ;
                break;

            case "team-member":
                email = ConfigurationReader.getProperty("team_member_email") ;
                password = ConfigurationReader.getProperty("team_member_password");
                break;
            case "team-leader":
                email = ConfigurationReader.getProperty("team_leader_email") ;
                password = ConfigurationReader.getProperty("team_leader_password") ;
                break;

            default:

                throw new RuntimeException("Invalid Role Entry :\n>> " + role + " <<");
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return credentials;

    }


}