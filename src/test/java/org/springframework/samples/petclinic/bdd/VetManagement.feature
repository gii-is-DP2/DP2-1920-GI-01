Feature: Vet Management
  As an admin I can manage the vets

  Scenario: Successful update of a vet (Positive)
    Given I am logged in the system as "admin1" with password "4dm1n"
    When I update an existing vet called "Test Dummy" with a new first name "Test2"
    Then the updated vet called "Test2 Dummy" appears in the listing

  Scenario: Unsuccessful update of a vet due to empty first name (Negative)
    Given I am logged in the system as "admin1" with password "4dm1n"
    When I update an existing vet called "James Carter" with a new first name ""
    Then an error appears in the vet form