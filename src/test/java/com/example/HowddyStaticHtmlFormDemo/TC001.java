package com.example.HowddyStaticHtmlFormDemo;
//package com.example.HowddyStaticHtmlForm;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.listeners.retry;
import utils.listeners.testNGListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import java.util.concurrent.TimeUnit;

@Listeners(testNGListener.class)
public class TC001 {
    //private WebDriver driver;

    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
    public static String remote_url = "http://localhost:4444/";
    public Capabilities capabilities;

    private static final Logger logger = LoggerFactory.getLogger(com.example.HowddyStaticHtmlForm.TC001.class);

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

    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
        public void setUp(String browser) throws Exception {

            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            prop.load(fis);

        //System.setProperty("webdriver.chrome.driver", prop.getProperty("webdriver.chrome.driver"));

        //Using selenium grid
        //Set the web drivers path to System Variable
        //Go to the location of "selenium-server-4.10.0.jar"
        //run the command "java -jar selenium-server-4.10.0.jar standalone"

        System.out.println("Test is running on "+ browser);
        logger.info("Test is running on "+ browser);
        if(browser.equals("firefox")) {
            capabilities = new FirefoxOptions();
        } else if (browser.equals("chrome")) {
            capabilities = new ChromeOptions();
        }



        driver.set(new RemoteWebDriver(new URL(remote_url), capabilities));
        baseUrl = prop.getProperty("baseUrl");
        driver.get().get(baseUrl);
        driver.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //js = (JavascriptExecutor) driver;
    }

    @Test(dataProvider = "formData", retryAnalyzer = retry.class)
    public void testTC001(String firstName, String lastName, String jobTitle, String radioButtonId,
                          String checkboxId, String selectMenuId, String selectOption, String datePickerId,
                          String date) throws Exception {
        driver.get().get(baseUrl);
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
        driver.get().quit();
        driver.remove();
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
        return driver.get().findElement(by);
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
            driver.get().findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.get().switchTo().alert();
            return true;
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetText() {
        try {
            org.openqa.selenium.Alert alert = driver.get().switchTo().alert();
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


