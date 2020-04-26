Feature: Trainer Management
  As an admin I can manage the trainers

  Scenario: Successful elimination of a trainer (Positive)
    Given I am logged in the system as "admin1" with password "4dm1n"
    When I delete an existing trainer called "Thomas Moon"
    Then a message "Trainer deleted successfully!" appears in the listing

  Scenario: Unsuccessful elimination of a trainer due to future rehab sessions (Negative)
    Given I am logged in the system as "admin1" with password "4dm1n"
    When I delete an existing trainer called "John Doe"
    Then a message "You can't delete a trainer that has future rehab sessions." appears in the listing