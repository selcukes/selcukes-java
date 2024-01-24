Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Subtraction
    When I subtract 7 to 2
    Then the result is 5

  @parallel
  Scenario Outline: Sum of <arg1> and <arg2>
    When I add <arg1> and <arg2>
    Then the result is <sum>

    Examples: Single digits
      | arg1 | arg2 | sum |
      | 1    | 2    | 3   |
      | 3    | 7    | 10  |
      | 6    | 8    | 14  |
      | 8    | 7    | 15  |
