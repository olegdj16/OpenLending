package com.openlending.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Data
public class Driver {
    static String browser;

    private static final String BASE_URL = ConfigurationReader.getProperty("openlendingurl");

    private static WebDriver driver;
    public static WebDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }

    @BeforeMethod
    public static void setupDriver() {
        String browserType = ConfigurationReader.getProperty("browser").toLowerCase();
        if (driver == null) {
            if (System.getProperty("BROWSER") == null) {
                browser = ConfigurationReader.getProperty("browser");
            } else {
                browser = System.getProperty("BROWSER");
            }
            System.out.println("Browser: " + browser);
            switch (browserType) {

                case "remote-chrome":
                    try {
                        // assign your grid server address
                        String gridAddress = "34.227.178.171";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                        desiredCapabilities.setBrowserName("chrome");
                        driver = new RemoteWebDriver(url, desiredCapabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "ie":
                case "internetexplorer":
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                default:
                    throw new RuntimeException("Invalid browser type provided: " + browserType);
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get(BASE_URL);
        }
    }

    @AfterMethod
    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

