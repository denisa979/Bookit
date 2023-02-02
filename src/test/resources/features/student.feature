@student
Feature: Create Student

  Scenario: Create student as a teacher and verify status code is 201
    Given I logged Bookit api as a "teacher"
    When I send POST request "/api/students/student" endpoint with following information
      | first-name      | Harold              |
      | last-name       | Finch               |
      | email           | hfinch@cydeo.com    |
      | password        | abc123              |
      | role            | student-team-member |
      | campus-location | VA                  |
      | batch-number    | 8                   |
      | team-name       | Nukes               |
    Then status code should be 201
    And I delete previously added student

    #Email information needs to be unique.Change email info from there to do it for you unique