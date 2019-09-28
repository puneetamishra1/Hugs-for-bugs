package com.nagarro.Autothon.Utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.nagarro.Autothon.GenericFunctions.BaseTestClass;
import com.nagarro.Autothon.GenericFunctions.Xls_Reader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class ExtentTestNGITestListener implements ITestListener, ISuiteListener  {
    private static String directory = "./extentreport/";
    private static String oldReportsDirectory = "./Archived_Reports/";
    private static String subDirectory = "screenshots/";
    private static String testCasesReport = "./TestCasesCreation/autothon_TestAutomation.xlsx";
    private static String  excelReport_New = "";
    private static String filePath = "";
    private static String filePathExtent = "";
    private static String qualifiedMethodName = "";
    private static String className = "";
    private static ExtentReports extent = ExtentManager.createInstance(directory + "Autothon_extentReport.html");
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
    private static WebDriver driver;
    public ExtentTest testToRemove = null;

    BaseTestClass base = new BaseTestClass();
    @Override
    public synchronized void onStart(ITestContext context) {

        className = context.getName();
        ExtentTest parent = extent.createTest(context.getName());
        parentTest.set(parent);
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
//        extent.flush();

    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        ExtentTest child = parentTest.get().createNode(result.getMethod().getMethodName());
        testToRemove = child;
        System.out.println("Testcase=========== " + result.getMethod().getMethodName()+" ==========has started executing");
        qualifiedMethodName = className + "_" + result.getMethod().getMethodName();
        test.set(child);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        test.get().pass("Test passed");
        String MethodName = result.getMethod().getMethodName();
        setExecutionStatus("PASSED", MethodName);
        base.log("Testcase============== " + result.getMethod().getMethodName()+" ============has Passed Successfully");
        extent.flush();

    }


    @Override
    public synchronized void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
        String MethodName = result.getMethod().getMethodName();
        setExecutionStatus("FAILED", MethodName);
        driver = BaseDriver.getDriver();
        captureScreenShot();
        try {
            test.get().addScreenCaptureFromPath(filePathExtent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        base.log("Testcase============== " + result.getMethod().getMethodName()+" ============== has failed");
        extent.flush();
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        test.get().skip(result.getThrowable());
        String MethodName = result.getMethod().getMethodName();
        setExecutionStatus("SKIPPED", MethodName);
        driver = BaseDriver.getDriver();
        captureScreenShot();
        try {
            test.get().addScreenCaptureFromPath(filePathExtent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @Override
//    public synchronized void onTestSkipped(ITestResult result) {
////       if(result.getThrowable().toString().contains("exception.SkipTestException")){
//        extent.removeTest(testToRemove);
//        test.get().skip(result.getThrowable());
//        String MethodName = result.getMethod().getMethodName();
//        setExecutionStatus("SKIPPED", MethodName);
//        driver = BaseDriver.getDriver();
////        captureScreenShot();
////        try {
////            test.get().addScreenCaptureFromPath(filePathExtent);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        base.log("Testcase================ " + result.getMethod().getMethodName()+" ============== has Skipped");
//        extent.flush();
//    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }


    /**************************************************************
     * Function Description:This method is used for capturing screenshots for failed testcases
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public void captureScreenShot() {
        synchronized (this) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ssaa");
                Date date = new Date();
                String dateName = dateFormat.format(date);
                filePathExtent = subDirectory + qualifiedMethodName + "_" + dateName + ".png";
                filePath = directory + filePathExtent;
                File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(f, new File(filePath));
                System.out.println("================== Took Screenshot through Selenium ==================");
            } catch (Exception e) {
                System.out.println(
                        "================== Some Exception occurred while getting screenshot ==================");
            }
        }
    }

    public void createDirectory() {
        try {
            File file = new File(directory + subDirectory);
            FileUtils.forceMkdir(file);
            FileUtils.cleanDirectory(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**************************************************************
     * Function Description:This method is used for archiving the extent report folder with date and time
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public static void copyReportToOld() {
        File oldDirectory = new File(directory);
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ssaa");
        String dateName = dateFormat.format(new Date());
        File newDirectory = new File(oldReportsDirectory + dateName);
        try {
            FileUtils.copyDirectory(oldDirectory, newDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**************************************************************
     * Function Description:This method is used for archiving the excel Sheet report
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public static void copyExcelReport() {
        File excelReport = new File(testCasesReport);
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ssaa");
        String dateName = dateFormat.format(new Date());
        excelReport_New = directory + "autothon_TestAutomation.xlsx";
        try {
            FileUtils.copyFile(excelReport, new File(excelReport_New));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(ISuite arg0) {
        createDirectory();
    }

    @Override
    public void onFinish(ISuite arg0) {

        copyExcelReport();
        copyReportToOld();
        sendReport();
    }


    /**************************************************************
     * Function Description:This method is used for setting us the execution status in Excel Sheet
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public void setExecutionStatus(String Status, String TestcaseID) {
        BaseTestClass base = new BaseTestClass();
        base.log(Status);
        Xls_Reader datatable = new Xls_Reader(base.getPropertyValue("autothon_TestAutomation"));
        int row = datatable.getCellRowNum(className, "TestCaseID", TestcaseID);
        datatable.setCellData(className, "Result", row, Status);
        datatable.setCellColor(className, "Result", row, Status);
    }



    /**************************************************************
     * Function Description:This method is called for sending an auto generated mail to the recipients with attached reports
     *Author: Puneeta Mishra, Date: 19-Nov-2018
     ***************************************************************/
    public void sendReport()
    {
//        final String username = "metcashautoct@gmail.com";
//        final String password = "nagarro@1";
        final String username = "puneeta.mishra@nagarro.com";
        final String password = "P@ssw0rd2345";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("puneeta.mishra@nagarro.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("puneeta.mishra@nagarro.com"));
            message.setSubject("Autothon Automation Report");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Hi All,"+"\n\nPFA automation execution report for Autothon."+"\n\nregards\nAutothon Automation Team");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String file = "./extentreport/Autothon_extentReport.html";
            String filename = "Autothon_extentReport.html";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);


            // Send the complete message parts
            message.setContent(multipart);



            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }


}