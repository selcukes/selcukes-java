package io.github.selcukes.testng.tests;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Steps {
    @When("the Maker starts a game")
    public void theMakerStartsAGame() {
        System.out.println("the Maker starts a game");

    }

    @Then("the Maker waits for a Breaker to join")
    public void theMakerWaitsForABreakerToJoin() {
        System.out.println("the Maker waits for a Breaker to join");
    }
}
