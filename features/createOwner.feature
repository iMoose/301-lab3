Feature: Register new owner
  Scenario: Registering a new owner
    Given I am connected to the WOF database
    And No owner with email "bob@gmail.com" exists
    When I try to register with this email
    Then This user should be present in the list of users