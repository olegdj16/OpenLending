package com.openlending.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        plugin = {"json:target/cucumber-reports/report.json"},
        features = "src/test/resources/features",
        glue = {"com/openlending/stepDefinitions"},
        dryRun = false,
        tags = "@no_duplicates",
        publish = true

)
public class TestRunner {
}