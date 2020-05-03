Feature: Vet updates medical record
  As a vet I can update medical records

  Scenario: Successful update of a medical record (Positive)
    Given I am logged in the system as "vet1" with password "v3t1"
    When I try to update a medical record with description "Fue atropellado por un camion" and status "Critico"
    Then The medical record is updated and both status "Critico" and description "Fue atropellado por un camion" are shown
    
  Scenario: Unsuccessful addition of a medical record without description or status (Negative)
  	Given I am logged in the system as "vet1" with password "v3t1"
  	When I try to update a medical record with description "" and status ""
  	Then an error appears in the medical record update form