Feature: Yahoo

  Scenario Outline: Yahoo Login - <Scenario>
    Given I open Yahoo page
    Then I see "Yahoo" in the title
    Examples:
      | Scenario  |
      | Example 1 |
      | Example 2 |

  Scenario: Different kind of opening
    Given I kinda open Yahoo page
    Then I am very happy