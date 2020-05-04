Feature: Medicine prescription
	As a vet I can prescript a medicine in a medical record to a given pet
	
	Scenario: Successful prescription of a medicine (Positive)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I prescript a new medicine called "Cat medicine" to a cat with a "test dose"
		Then the medicine appears in the corresponding medical record
		
	Scenario: Unsuccessful prescription of a medicine (Negative)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I prescript a new medicine called "" to a cat with a "test dose"
		Then an error appears in the prescription form