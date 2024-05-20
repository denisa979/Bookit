Feature: Add new team API and DB validation
@us4 @db
  Scenario: Post new team and verify in database
    Given I logged Bookit api as a "teacher"
    When I sends POST request to "/api/teams/team" with following info:
      | campus-location | VA            |
      | batch-number    | 28          |
      | team-name       | Green Way180|
    Then status code should be 201
    And Database should persist same team info
    And User deletes previously created team

    # POST Team
    # Store teamID info
    # Get same tem info from database with using ID from API response
       # To get related information you need use JOINS
    # Delete team that we generate


    # Which one is EXPECTED ?  Since we add data from API, DB needs to show this info as we provide
    # EXPECTED --> API
    # ACTUAL   --> DB