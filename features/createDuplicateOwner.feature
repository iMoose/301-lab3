Feature: Register a duplicate owner
  Scenario: Registering a duplicate owner
    Given: I am connected to the WOF database
    And: An owner with email "bob@gmail.com" exists
    When I try to register with this email
    Then I should get an error and a console statement saying that the user already exists