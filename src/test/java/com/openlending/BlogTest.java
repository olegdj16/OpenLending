package com.openlending;

import com.openlending.utils.ConfigurationReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BlogTest {

    public static WebDriver driver;

    public static void main(String[] args) {

        String browserType = ConfigurationReader.getProperty("browser").toLowerCase();
        switch (browserType) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            default:
                System.out.println("Unsupported browser type: " + browserType);
                return;
        }

        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://www.openlending.com/");

            WebDriverWait wait = new WebDriverWait(driver, 20);
            // Click on the accept button for cookies/consent
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='cmplz-btn cmplz-accept']")));
            acceptButton.click();
            // Navigate to Resources page
            WebElement resourcesLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Resources")));
            resourcesLink.click();

            wait = new WebDriverWait(driver, 20);
            Map<String, Integer> blogEntries = new HashMap<>();
            while (true) {
                List<WebElement> currentEntries = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("lenders-featured-link")));
                for (WebElement entryElement : currentEntries) {
                    String entryText = entryElement.getText().trim();
                    if (!entryText.isEmpty()) {
                        blogEntries.put(entryText, blogEntries.getOrDefault(entryText, 0) + 1);
                    }
                }

                List<WebElement> loadMoreButtons = driver.findElements(By.className("facetwp-load-more"));
                if (loadMoreButtons.size() == 0) {
                    break;
                }

                WebElement loadMoreButton = loadMoreButtons.get(0);
                if (loadMoreButton.isDisplayed()) {
                    loadMoreButton = wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(loadMoreButton)));
                    loadMoreButton.click();

                    int previousSize = currentEntries.size();
                    wait.until(driver -> driver.findElements(By.className("lenders-featured-link")).size() > previousSize);
                } else {
                    break;
                }
            }

            // Check for duplicates
            boolean hasDuplicates = false;
            for (Map.Entry<String, Integer> entry : blogEntries.entrySet()) {
                if (entry.getValue() > 1) {
                    hasDuplicates = true;
                    System.out.println("Duplicate blog entry found: " + entry.getKey());
                }
            }

            if (!hasDuplicates) {
                System.out.println("No duplicate blog entries found.");
            }

        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
