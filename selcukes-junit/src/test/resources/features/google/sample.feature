Feature: Sample Guess the word

  @ex
  Scenario: Google starts a game
    When the Maker starts a game
    Then the Maker waits for a Breaker to join

  @ex1
  Scenario: Google starts a game
    When the Maker starts a game