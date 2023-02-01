package com.bookit.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ApiStepDefs {

    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {

    }
    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {

    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {

    }
    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {

    }
    @Then("role is {string}")
    public void role_is(String expectedRole) {

    }
}
