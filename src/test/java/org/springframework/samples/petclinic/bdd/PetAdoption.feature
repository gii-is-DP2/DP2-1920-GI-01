Feature: Pet adoption
	As a vet I can adopt a homeless pet to a person
	
	Scenario: Successful pet adoption (Positive)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I adopt a new pet to an owner called "George Franklin"
		Then the new pet appears in the listing
		
	Scenario: Unsuccessful pet adoption (Negative)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I adopt a new pet to an owner called ""
		Then an error appears in the adoption form