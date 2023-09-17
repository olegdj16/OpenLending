package com.openlending.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.*;

public class ResourcesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public ResourcesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void clickAcceptButton() {
        WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='cmplz-btn cmplz-accept']")));
        acceptButton.click();
    }

    public void clickLinkByText(String linkText) {
        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
        linkElement.click();
    }

    public Map<String, Integer> navigateAndCaptureEntries(String buttonName) {
        Map<String, Integer> blogEntries = new HashMap<>();
        while (true) {
            List<WebElement> currentEntries = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("lenders-featured-link")));
            for (WebElement entryElement : currentEntries) {
                String entryText = entryElement.getText();
                blogEntries.put(entryText, blogEntries.getOrDefault(entryText, 0) + 1);
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
        return blogEntries;
    }

    public List<WebElement> getParagraphElements() {
        List<WebElement> paragraphElements = driver.findElements(By.xpath("//div[@class='paragraph-p2']"));
        for (WebElement element : paragraphElements) {
            System.out.println(element.getText().trim());
        }
        return paragraphElements;
    }
}
