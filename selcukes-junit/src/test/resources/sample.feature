Feature: Sample Guess the word Demo

  @ex
  Scenario: Maker starts a game
    When the Maker starts a game
    Then the Maker waits for a Breaker to join

  @ex1
  Scenario: Maker starts a game
    When the Maker starts a game