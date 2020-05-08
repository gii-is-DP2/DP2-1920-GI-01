Feature: Vet seeing pets visits
	As a vet I see all visits certain pet has
	
	Scenario: Vet can see owners visits (Positive)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I go to the "Find owners" page
		Then After finding owner "Eduardo Rodriquez" I can see his pets visits in the section "Pets and Visits"
	
	
Scenario: Vet cannot see owners visits (Negative)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I go to the "Find owners" page
		Then an error appears "has not been found"