Feature: Visit Creation
	As a vet I can create new visits
	
	Scenario: Successful creation of a new visit (Positive)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I create a new visit with description "Description" and date "2020/10/01"
		Then the new visit appears in the owners details page
	
	Scenario: Unsuccessful creation of a visit due to incorrect data (Negative)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I create a new visit with description "Description" and date "2020/01/01"
		Then an error appears in the visit form