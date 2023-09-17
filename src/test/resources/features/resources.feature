Feature: Verification of Blog Entries

@no_duplicates
  Scenario: Verify no duplicate blog entries
    When I click on the "Resources" link
    And I navigate through each page of results using the "Load More" button
    Then there should be no duplicate blog entries

