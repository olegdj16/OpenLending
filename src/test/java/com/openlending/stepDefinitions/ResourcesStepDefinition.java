package com.openlending.stepDefinitions;

import com.openlending.pages.ResourcesPage;
import com.openlending.utils.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openlending.utils.Driver.getDriver;

public class ResourcesStepDefinition{
    private ResourcesPage resourcesPage;
    private Scenario scenario;
    private Map<String, Integer> blogEntries;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        this.resourcesPage = new ResourcesPage(getDriver());
    }

    @When("I click on the {string} link")
    public void i_click_on_the_link(String linkText) {
        resourcesPage.clickAcceptButton();
        resourcesPage.clickLinkByText(linkText);
    }

    @When("I navigate through each page of results using the {string} button")
    public void i_navigate_through_each_page_of_results_using_the_button(String buttonName) {
        blogEntries = resourcesPage.navigateAndCaptureEntries(buttonName);
    }

    @Then("there should be no duplicate blog entries")
    public void there_should_be_no_duplicate_blog_entries() {
        List<WebElement> elements = resourcesPage.getParagraphElements();
        Map<String, Integer> textEntries = new HashMap<>();
        for (WebElement element : elements) {
            String text = element.getText().trim();
            textEntries.put(text, textEntries.getOrDefault(text, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : textEntries.entrySet()) {
            if (entry.getValue() > 1) {
                scenario.log("Duplicate text in paragraph-p2 elements: \"" + entry.getKey() + "\" appeared " + entry.getValue() + " times.");
            }
        }

        int totalDuplicates = (int) textEntries.values().stream().filter(val -> val > 1).count();

        if (totalDuplicates == 0) {
            scenario.log("Step 'there should be no duplicate blog entries' PASSED");
        } else {
            scenario.log("Step 'there should be no duplicate blog entries' FAILED: There are duplicate texts.");
            throw new AssertionError("Duplicates found.");
        }
    }

    @After
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}
