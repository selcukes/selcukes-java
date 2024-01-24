Feature: Google

  Scenario Outline: Google Login - <Scenario>
    Given I open Google page
    Then I see "Google" in the title
    Examples:
      | Scenario  |
      | Example 1 |
      | Example 2 |

  Scenario: Different kind of opening
    Given I kinda open Google page
    Then I am very happy