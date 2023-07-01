package com.HowddyStaticHtmlFormDemo;
//package com.example.HowddyStaticHtmlForm;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class TC001 {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://formy-project.herokuapp.com/form";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testTC001() throws Exception {
        driver.get(baseUrl);
        setFirstName("John");
        setLastName("Amber");
        setJobTitle("Tester");
        selectRadioButton("radio-button-2");
        selectCheckbox("checkbox-1");
        selectOptionFromMenu("select-menu", "2-4");
        selectDate("datepicker", "06/28/2023");
        clickSubmitButton();

        try {
            Assert.assertEquals(getSuccessMessage(), "The form was successfully submitted!");
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!verificationErrorString.isEmpty()) {
            Assert.fail(verificationErrorString);
        }
    }

    private WebElement findElement(By by) {
        return driver.findElement(by);
    }

    private void clickElement(By by) {
        findElement(by).click();
    }

    private void clearElement(By by) {
        findElement(by).clear();
    }

    private void sendKeysToElement(By by, String text) {
        findElement(by).sendKeys(text);
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetText() {
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    // Page Object Methods

    private void setFirstName(String firstName) {
        By firstNameField = By.id("first-name");
        clickElement(firstNameField);
        clearElement(firstNameField);
        sendKeysToElement(firstNameField, firstName);
    }

    private void setLastName(String lastName) {
        By lastNameField = By.id("last-name");
        clearElement(lastNameField);
        sendKeysToElement(lastNameField, lastName);
    }

    private void setJobTitle(String jobTitle) {
        By jobTitleField = By.id("job-title");
        clearElement(jobTitleField);
        sendKeysToElement(jobTitleField, jobTitle);
    }

    private void selectRadioButton(String radioButtonId) {
        By radioButton = By.id(radioButtonId);
        clickElement(radioButton);
    }

    private void selectCheckbox(String checkboxId) {
        By checkbox = By.id(checkboxId);
        clickElement(checkbox);
    }

    private void selectOptionFromMenu(String menuId, String optionText) {
        By menu = By.id(menuId);
        clickElement(menu);
        Select select = new Select(findElement(menu));
        select.selectByVisibleText(optionText);
    }

    private void selectDate(String datepickerId, String date) {
        By datepicker = By.id(datepickerId);
        clickElement(datepicker);
        By dateLocator = By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[32]");
        clickElement(dateLocator);
    }

    private void clickSubmitButton() {
        By submitButton = By.linkText("Submit");
        clickElement(submitButton);
    }

    private String getSuccessMessage() {
        By successMessage = By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Thanks for submitting your form'])[1]/following::div[1]");
        return findElement(successMessage).getText();
    }
}


