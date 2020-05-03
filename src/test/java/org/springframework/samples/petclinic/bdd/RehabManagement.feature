Feature: New rehabilitation creation
As a trainer I can add rehabilitations

Scenario: Successful creation of a rehabilitation (Positive)
Given I am logged in the system as "trainer1" with password "tr41n3r"
When I create a new rehabilitation for a pet whose owner last name is "Rodriquez"
Then a new rehabilitation with a description "First rehab" appears in the list 

Scenario: Unsuccessful creation of a rehabilitation (Negative)
Given I am logged in the system as "trainer1" with password "tr41n3r"
When I create a new rehabilitation for a pet whose owner last name is "Rodriquez"
Then an error appears in the rehabilitation form
