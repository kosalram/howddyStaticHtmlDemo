package com.example.HowddyStaticHtmlForm;

import com.aventstack.extentreports.Status;

//import ch.qos.logback.core.status.Status;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.listeners.retry;
import utils.listeners.testNGListener;

@Listeners(testNGListener.class)
public class TC001 {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;
    private static final Logger logger = LoggerFactory.getLogger(TC001.class);

    private static final String FILE_PATH = "src/test/resources/testData/testdata.xlsx";

    private static ExtentReports extent;
    private static ExtentTest test;

    @BeforeSuite
    public void before() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("src/test/java/utils/testOutput/extent-report.html");
        htmlReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }


    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://formy-project.herokuapp.com/form";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;

    }

    //Using data provider
    @Test(dataProvider = "formData", retryAnalyzer = retry.class)
    public void testTC001(String firstName, String lastName, String jobTitle, String radioButtonId,
                          String checkboxId, String selectMenuId, String selectOption, String datePickerId,
                          String date) throws Exception {
        driver.get(baseUrl);
        System.out.println(firstName+lastName+jobTitle+radioButtonId+checkboxId+selectMenuId+selectOption+datePickerId+date);
        setFirstName(firstName);
        setLastName(lastName);
        setJobTitle(jobTitle);
        selectRadioButton(radioButtonId);
        selectCheckbox(checkboxId);
        selectOptionFromMenu(selectMenuId, selectOption);
        selectDate(datePickerId, date);
        clickSubmitButton();

        test = extent.createTest("Test Case: TC001");
        test.log(Status.INFO, "Test data: " + firstName + ", " + lastName + ", " + jobTitle +
                ", " + radioButtonId + ", " + checkboxId + ", " + selectMenuId + ", " +
                selectOption + ", " + datePickerId + ", " + date);

        try {
            Assert.assertEquals(getSuccessMessage(), "The form was successfully submitted!");
            logger.info("The form was successfully submitted!");
            System.out.println("Form Successfully Submitted");

        } catch (Error e) {
            verificationErrors.append(e.toString());
            test.log(Status.FAIL, "Form submission failed");
            test.log(Status.ERROR, e);
        }
    }

//    @DataProvider(name = "formData")
//    public Object[][] getFormData() {
//        return new Object[][]{
//                {"John", "Amber", "Tester", "radio-button-2", "checkbox-1", "select-menu", "2-4", "datepicker", "06/28/2023"}
//                // Add more test data as needed
//            };
//        };

    @DataProvider(name = "formData")
    public Object[][] getFormData() throws IOException {
        FileInputStream fis = new FileInputStream(FILE_PATH);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
//        Sheet sheet = workbook.getSheet("Sheet1");
        int rowCount = sheet.getLastRowNum();
        int colCount = sheet.getRow(0).getLastCellNum();

        Object[][] data = new Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                data[i - 1][j] = cell.getStringCellValue();
            }
        }

        workbook.close();
        fis.close();

        return data;
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!verificationErrorString.isEmpty()) {
            Assert.fail(verificationErrorString);
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
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


