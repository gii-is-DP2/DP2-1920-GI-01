Feature: Intervention creation
	As a vet I can create new interventions
	
	Scenario: Successful creation of a new intervention (Positive)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I create a new intervention with description "Descripcion" and intervention time "1" with intervention date "2020/10/01"
		Then the new intervention appears in the owners details page
	
	Scenario: Unsuccessful creation of a intervention due to incorrect data (Negative)
		Given I am logged in the system as "vet1" with password "v3t1"
		When I create a new intervention with description "Description" and intervention time "1" with intervention date "2020/01/01"
		Then an error appears in the intervention form