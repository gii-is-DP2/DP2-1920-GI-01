Feature: Homeless Pet Management
  As a vet I can manage the homeless pets

  Scenario: Successful addition of a homeless pet (Positive)
    Given I am logged in the system as "vet1" with password "v3t1"
    When I add a new homeless pet called "Hodor" born on "2018/08/17" of type "dog"
    Then the new homeless pet called "Hodor" appears in the listing
    
  Scenario: Unsuccessful addition of a homeless pet due to birthdate in the future (Negative)
  	Given I am logged in the system as "vet1" with password "v3t1"
  	When I add a new homeless pet called "Hodor" born on "2200/02/20" of type "dog"
  	Then an error appears in the homeless pet form