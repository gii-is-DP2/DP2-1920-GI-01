Feature: Vet deletes medical record
  As a vet I can delete medical records

  Scenario: Successful deletion of a medical record (Positive)
    Given I am logged in the system as "vet1" with password "v3t1"
    When I try to delete a medical record
    Then it is deleted and it is not shown in the list
    
  Scenario: Unsuccessful deletion of a medical record (Negative)
  	Given I am logged in the system as "vet1" with password "v3t1"
  	When I try to delete a medical record
  	Then an error page appears