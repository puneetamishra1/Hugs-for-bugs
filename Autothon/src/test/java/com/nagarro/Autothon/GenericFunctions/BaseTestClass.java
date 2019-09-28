package com.nagarro.Autothon.GenericFunctions;

import com.nagarro.Autothon.Utils.BaseDriver;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BaseTestClass extends BaseDriver {
    public String winHandle;
    public String userDrirectory = System.getProperty("user.dir");

    boolean testResults = false;

    public static String environment = System.getProperty("environment", "local");
    public String execution_Type = System.getProperty("execution_Type", "regression");
    public String driver_Type = System.getProperty("driver_Type", "Chrome");


    Xls_Reader datatable;

    @BeforeTest
    public void init() throws Exception {

        //This method is used to load the property file
        System.out.println(environment);
        System.out.println(execution_Type);
        System.out.println(driver_Type);

        //        setPropertyValue(environment,execution_Type,driver_Type);
        System.setProperty("environment", environment);
        System.setProperty("execution_Type", execution_Type);
        System.setProperty("driver_Type", driver_Type);

        driver = startDriver(System.getProperty("driver_Type", "Chrome"));
        BaseDriver.setDriver(driver);
        driver.manage().window().maximize();

    }

    /***********************************************************************************************
     * Function Description : this function is use to print the log using log4j properties file
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public void log(String logValue) {
        Logger logger = Logger.getLogger(logValue);
        PropertyConfigurator.configure(getPropertyValue("Log4jFilePath"));
        logger.info(logValue);
    }


    /***********************************************************************************************
     * Function Description : Sets implicit Wait by accepting timeout in seconds
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/


    public String setImplicitWaitInSeconds(int timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
        return "Timeout set to " + timeOut + " seconds.";
    }

    /***********************************************************************************************
     * Function Description : Sets implicit Wait by accepting timeout in milliseconds
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public String SetImplicitWaitInMilliSeconds(int timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.MILLISECONDS);
        return "Timeout set to " + timeOut + " milli seconds.";
    }

    /***********************************************************************************************
     * Function Description : Initiates the driver. Set the browserType variable before calling this function
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public WebDriver startDriver(String browser) {

        if (browser.equalsIgnoreCase("firefox")) {


            FirefoxOptions options = new FirefoxOptions();
            options.setBinary("./drivers/Mozilla Firefox/firefox.exe");
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability("marionette", true);
            capabilities.setCapability("moz:firefoxOptions", options);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            log("Launching Firefox");
            System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
            driver = new FirefoxDriver(capabilities);

        } else if (browser.equalsIgnoreCase("chrome")) {

            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", userDrirectory);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", chromePrefs);
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            cap.setCapability(ChromeOptions.CAPABILITY, options);
            log("Launching Chrome");
            System.setProperty("webdriver.chrome.driver", getPropertyValue("ChromeDriverPath"));
            driver = new ChromeDriver(cap);

        } else {
            // If no browser passed throw exception
            System.out.println("Browser is not correct");
        }
        return driver;
    }


    /***********************************************************************************************
     * Function Description : gets the handle for the current window
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public void getWindowHandle() {
        winHandle = driver.getWindowHandle();
    }

    /***********************************************************************************************
     * Function Description : Switches to the most recent window opened
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public void switchtoNewWindow() {
        getWindowHandle();
        for (String windowsHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandle);
        }
    }

    /***********************************************************************************************
     * Function Description : WaitForCompletePageLoad:
     * This function is used when we want the complete page to get load without dependency on element presence.
     * Usefulness/Functionality : It can be used in cases where the JS is loaded only after complete page is loaded.
     For example: In case of Suggester testing where suggestions are loaded only after complete page is loaded.
     So, instead of using excessive sleep every time, this method can be used effectively.
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public void waitForCompletePageLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }


    /***********************************************************************************************
     * Function Description : Closes the window
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public void closeNewWindow() {
        driver.close();
    }


    /***********************************************************************************************
     * Function Description : Explicit wait
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public void goToSleep(int TimeInMillis) {
        try {
            Thread.sleep(TimeInMillis);
        } catch (Exception e) {
        }
    }

    /**************************************************************
     * Function Description: Enters a provided text into a text box Function
     * author: Puneeta Mishra, date: 01-May-2018
     ***************************************************************/
    public String fill_Txt(String xpath, String data) {
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(data);
        return "Element entered in text box successfully";
    }

    /**************************************************************
     * Function Description:To scroll the page Function parameters: String xpath
     * author: Puneeta Mishra, date: 01-May-2018
     ***************************************************************/
    public void scroll_The_Page(String xpath) {
        WebElement Element = driver.findElement(By.xpath(xpath));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", Element);
//        System.out.println(loc);
//        Point loc = driver.findElement(By.xpath(xpath)).getLocation();
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("javascript:window.scrollBy(0," + loc.y + ")");
    }

    /***********************************************************************************************
     * Function Description : It returns the value for the provided key from config file
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public String getPropertyValue(String propertyName) {

        String propertyValue = "";
        try {
            FileInputStream input = new FileInputStream("./configuration/Config.properties");
            Properties prop = new Properties();
            prop.load(input);
            propertyValue = prop.getProperty(propertyName);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return propertyValue;
    }

    public void setPropertyValue(String environment, String execution_Type, String driver_Type) {
        log("Setting config variables");
        System.out.println("-------" + getPropertyValue("environment"));
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            // set the properties value
            prop.setProperty("environment", environment);
            prop.setProperty("execution_Type", execution_Type);
            prop.setProperty("driver_Type", driver_Type);
            prop.setProperty("autothon_TestAutomation", "./TestCasesCreation/autothon_TestAutomation.xlsx");
            prop.setProperty("Log4jFilePath", "Log4j.properties");
            prop.setProperty("Browser", driver_Type);
            prop.setProperty("ChromeDriverPath", "./drivers/chromedriver.exe");


            // save properties to project root folder
            prop.store(new FileOutputStream("./configuration/Config.properties"), null);

        } catch (IOException io) {
            io.printStackTrace();
        }


//        System.out.println(prop.getProperty("environment"));
//        System.out.println(prop.getProperty("execution_Type"));
//        System.out.println(prop.getProperty("driver_Type"));
        System.out.println("-------" + getPropertyValue("execution_Type"));

    }

    /***********************************************************************************************
     * Function Description : This method skips the test case on the basis of the runmode present in testcase sheet
     * author: Puneeta Mishra, date: 03-May-2018
     * *********************************************************************************************/
    public void verifyTestIsExecutable(String testName, String TestClassName) {

        datatable = new Xls_Reader(getPropertyValue("autothon_TestAutomation"));
        int rowNumber = datatable.getCellRowNum(TestClassName, "TestCaseID", testName);
        if (execution_Type.equalsIgnoreCase("Regression")) {
            if (datatable.getCellData(TestClassName, "RunAutomation_Regression", rowNumber).equalsIgnoreCase("N")) {
                System.out.println("Ignored Test Case  :" + testName);
                throw new SkipException("Run Mode Set to No");
//                throw new SkipTestException("Run Mode Set to No");
            }
        }
        if (execution_Type.equalsIgnoreCase("Sanity")) {
            if (datatable.getCellData(TestClassName, "RunAutomation_Sanity", rowNumber).equalsIgnoreCase("N")) {
                System.out.println("Ignored Test Case  :" + testName);
                throw new SkipException("Run Mode Set to No");
            }


        }
        if (execution_Type.equalsIgnoreCase("Smoke")) {
            if (datatable.getCellData(TestClassName, "RunAutomation_Smoke", rowNumber).equalsIgnoreCase("N")) {
                System.out.println("Ignored Test Case  :" + testName);
                throw new SkipException("Run Mode Set to No");
            }


        }
    }

    /**************************************************************
     * Function Description:To get text of a WebElement
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public String getElementText(String xpath) {
        return driver.findElement(By.xpath(xpath)).getText();
    }


    /***********************************************************************************************
     * Function Description : Selects the provided value in a customized drop down
     *Author: Puneeta Mishra, Date: 09-May-2018
     ***********************************************************************************************/
    public void select_DropDown(String element, String xpath, String selectText) {
        goToSleep(2000);
        driver.findElement(By.xpath(element)).click();
        if (driver.findElements(By.xpath(xpath)).size() > 0) {
            List<WebElement> DropDownValueContainer;
            DropDownValueContainer = driver.findElements(By.xpath(xpath));

            for (WebElement option : DropDownValueContainer) {
                if (option.getText().equals(selectText)) {
                    option.click();
                    break;
                }
            }
        }

    }

    /*****************************************************************************************************
     * Function Description : This Function verifies whether given element present in the list or not.
     * Author: Puneeta Mishra, date: 08-May-2018
     * *************************************************************************************************/


    public Boolean checkElementPresentOrNotInList(String value, String locator) {


        List<WebElement> List = driver.findElements(By.xpath(locator));

        for (WebElement element : List) {

            if ((element.getText()).equals(value)) {
                testResults = true;
                break;
            }
        }
        return testResults;
    }

    /*****************************************************************************************************
     * Function Description : This Function is use to get and print test case execution status after end
     of each test case.
     * Author: Puneeta Mishra, date: 16-May-2018
     * *************************************************************************************************/

    public void testCaseExecutionStatus(ITestResult result, String testName) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                log("");
                log("***********************" + testName + " has PASSED. " + "**************************");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                log("");
                log("***********************" + testName + " has FAILED. " + "**************************");
            } else if (result.getStatus() == ITestResult.SKIP) {
                log("");
                log("***********************" + testName + " has SKIPPED. " + "**************************");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*****************************************************************************************************
     * Function Description : This Function is use to get one day previous date than current date
     * Author: Puneeta Mishra, date: 24-May-2018
     * *************************************************************************************************/
    public String getPreviousDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/");

        // Create a calendar object with today date. Calendar is in java.util package.
        Calendar calendar = Calendar.getInstance();

        // Move calendar to yesterday
        calendar.add(Calendar.DATE, -1);

        // Get current date of calendar which point to the yesterday now
        Date yesterday = calendar.getTime();

        return dateFormat.format(yesterday);
    }


    /*****************************************************************************************************
     * Function Description : This Function is use to get random future date in "dd/MM/yyyy" format.
     * Author: Puneeta Mishra, date: 25-May-2018
     @return : date in dd/MM/yyyy format.
      * *************************************************************************************************/
    public String getRandomFutureDate() {

        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Date date = new Date();
        //return (dateFormat.format(date));


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        String str_date1 = (dateFormat.format(cal.getTime()));
        String str_date2 = "31/12/2099";

        try {
            cal.setTime(dateFormat.parse(str_date1));

            Long value1 = cal.getTimeInMillis();

            cal.setTime(dateFormat.parse(str_date2));
            Long value2 = cal.getTimeInMillis();

            long value3 = (long) (value1 + Math.random() * (value2 - value1));
            cal.setTimeInMillis(value3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(cal.getTime());
    }

    /*******************************************************************************************
     * Function Description : This Function is use to get  fixed future  date in "dd/MM/yyyy" format. Need to pass number of days.
     * Author: Kapil Gupta, date: 14-June-2018
     @return : Fixed future date in dd/MM/yyyy format.
      * ****************************************************************************************/
    public String getFixedFutureDate(int Add_Days) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, Add_Days); // Adding 5 days
        String output = sdf.format(c.getTime());
        return output;
    }

    /*******************************************************************************************
     * Function Description : This Function is use to get  current date in "dd/MM/yyyy" format.
     * Author: Puneeta Mishra, date: 25-May-2018
     @return : date in dd/MM/yyyy format.
      * ****************************************************************************************/
    public String getCurrentDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return (dateFormat.format(date));
    }


    /*****************************************************************************************************
     * Function Description : This Function move your cursor to particular element.
     * Author: Puneeta Mishra, date: 26-May-2018.
     @param : Element on which you want to hover cursor.
      * *************************************************************************************************/


    public void moveCursorTo(WebElement element) {


        Actions builder = new Actions(driver);
        Action moveElement = builder
                .moveToElement(element)
                .build();
        moveElement.perform();
    }

    /***********************************************************************************************
     * Function Description : This method used to check if the file exist
     * author: Puneeta mishra, date: 23-May-2018
     * *********************************************************************************************/
    public boolean downloadedfileExists(String filename, String extension) {
        File newFile = new File(userDrirectory + "/" + filename + "." + extension);
        List<String> results = new ArrayList<String>();
        File[] files = new File(userDrirectory).listFiles();
        for (File file : files) {
            if (file.getName().contains(filename)) {
                file.renameTo(newFile);
                return true;

            }
        }
        return false;

    }


    /***********************************************************************************************
     * Function Description : This method used delete an existing file
     * author: Puneeta mishra, date: 24-May-2018
     * *********************************************************************************************/
    public boolean deleteFile(String filename) {

        List<String> results = new ArrayList<String>();
        File[] files = new File(userDrirectory).listFiles();
        for (File file : files) {
            if (file.getName().contains(filename)) {
                file.delete();
                return true;
            }
        }
        return false;

    }


    /***********************************************************************************************
     * Function Description : This method used to generate random number.
     * author: Kapil Gupta, date: 04-june-2018
     * @return : Return random integer value.
     * *********************************************************************************************/
    public int randomNumber() {
        Random random = new Random();
        int value = random.nextInt(99999);
        return value;
    }

    /***********************************************************************************************
     * Function Description : This method used to set value in the CSV file.
     * author: Kapil Gupta, date: 12-june-2018
     * @return : Store value in the CSV file at specified cell
     * *********************************************************************************************/

    public void setCSV(String fileToUpdate, int row, int col, String data) throws IOException {
        File inputFile = new File(fileToUpdate);
        // Read existing file
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(inputFile), ',');
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String[]> csvBody = null;
        try {
            csvBody = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get CSV row column  and replace with by using row and column
        csvBody.get(row)[col] = data;
        reader.close();
        // Write to CSV file which is open
        CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
    }

    /***********************************************************************************************
     * Function Description : This method is used to get value from the CSV file.
     * author: Kapil Gupta, date: 12-june-2018
     * @return : Fetch value from the CSV file (specified cell)
     * *********************************************************************************************/

    public String getCSV(String fileToUpdate, int row, int col) throws IOException {
        File inputFile = new File(fileToUpdate);
        // Read existing file
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(inputFile), ',');
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String[]> csvBody = null;
        try {
            csvBody = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get CSV row column  and replace with by using row and column
        String data = csvBody.get(row)[col];
        reader.close();
        return data;
    }

    /***********************************************************************************************
     * Function Description : This method is used to generate GTIN
     * author: Puneeta Mishra, date: 26-june-2018
     * *********************************************************************************************/
    public String GTIN_generatorNew() {
        long num, multipleofTen = 0, gtin = 0;
        int count = 12, i;
        String dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String date = dateFormat.substring(5, 18);
        date = date.replace(".", "");
        int data = Integer.parseInt(date);
        long generatedLong = 999000000000L + data;
        String TradeGtin = "" + generatedLong;

        while (generatedLong != 0) {
            num = generatedLong % 10;
            if (count % 2 == 0) {
                gtin = gtin + num * 3;
                count--;
                generatedLong = generatedLong / 10;
            } else {
                gtin = gtin + num * 1;
                count--;
                generatedLong = generatedLong / 10;

            }
        }

        if (gtin % 10 == 0) {
            multipleofTen = 10;
        } else {
            multipleofTen = gtin % 10;
        }
        long newgting = 10 - multipleofTen;
        TradeGtin = TradeGtin + newgting;
        log("Trade Gtin Generated is :" + TradeGtin);
        return TradeGtin;
    }

    public String ConsumerBase_PLU() {
        String dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String date = dateFormat.substring(11, 19);
        date = date.replace(".", "");
        String consumerbasePLU = "" + date;
        log("Consumer Base PLU :-- " + consumerbasePLU);
        return consumerbasePLU;
    }

    /***********************************************************************************************
     * Function Description : This method used to get default selected value of a dropdown
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/
    public String getDefaultSelectedDropdownValue(String element) {

        Select select = new Select(driver.findElement(By.xpath(element)));
        String selectedValue = select.getFirstSelectedOption().getText();
        return selectedValue;
    }

    public void waitUntil(String element, int timeInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));

    }

    @AfterSuite
    public void cleanup() throws Exception {
        Runtime.getRuntime().exec("taskkill /F /IM ChromeDriver.exe");
    }


}