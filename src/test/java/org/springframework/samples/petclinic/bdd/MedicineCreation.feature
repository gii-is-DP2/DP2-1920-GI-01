Feature: Medicine Creation
	As a vet I can create new medicines
	
	Scenario: Successful creation of a new medicine (Positive)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I create a new medicine called "Prueba" and maker "prueba" with expiration date "2030/01/01" of type "cat"
		Then the new medicine appears in the listing
	
	Scenario: Unsuccessful creation of a medicine due to incorrect date (Negative)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I create a new medicine called "Prueba" and maker "prueba" with expiration date "2000/01/01" of type "cat"
		Then an error appears in the medicine form