Feature: Owner sees Vet personal information
	As a owner I can see Vets personal information
	
	Scenario: Successful listing of a vet information (Positive)
		Given I am logged in the system as "owner1" with password "0wn3r"
		When I go to the profile of the Veterinarian "James Carter"
		Then his name "James Carter" appears in the page
	
	Scenario: Unsuccessful listing of a vet information (Negative)
		Given I am logged in the system as "admin1" with password "4dm1n"
		When I go to the profile of the Veterinarian "James Carter"
		Then the loggin page appears