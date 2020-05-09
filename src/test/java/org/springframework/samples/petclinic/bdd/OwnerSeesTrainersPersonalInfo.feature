Feature: Owner seeing trainers personal information
As an owner I can see all trainer personal information

Scenario: Successfully opening trainers personal information page (Positive)
Given I am logged in the system as "trainer1" with password "tr41n3r"
When I go to the trainers "John Doe" personal information page
Then I can see this trainers name "John Doe" and his email "acme@mail.com" 

Scenario: Unsuccessfully trying to open trainers personal information page (Negative)
Given I am logged in the system as "trainer1" with password "tr41n3r"
When I go to the trainers "John Doe" personal information page
Then an error "Trainer not found!" appears 


