Feature: Vet creates medical record
   As a vet I can create medical records

  Scenario: Successful addition of a medical record (Positive)
    Given I am logged in the system as "vet1" with password "v3t1"
    When I add a new medical record with description "La herida progresa adecuadamente" and status "Sano"
    Then The new medical record with status "Sano" and description "La herida progresa adecuadamente" is shown
    
  Scenario: Unsuccessful addition of a medical record without description or status (Negative)
  	Given I am logged in the system as "vet1" with password "v3t1"
  	When I add a new medical record with description "" and status ""
  	Then an error appears in the medical record form