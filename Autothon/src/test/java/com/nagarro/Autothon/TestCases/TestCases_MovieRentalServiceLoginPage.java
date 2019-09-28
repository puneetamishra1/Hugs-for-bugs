package com.nagarro.Autothon.TestCases;

import com.nagarro.Autothon.GenericFunctions.BaseTestClass;
import com.nagarro.Autothon.PageObjects.Admin_HomePage;
import com.nagarro.Autothon.PageObjects.MovieRentalService_LoginPage;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class TestCases_MovieRentalServiceLoginPage extends BaseTestClass {


    public String testName = null;
    public String TestClassName = null;

    @BeforeMethod
    public void beforeMethod(Method method) {
        driver.manage().deleteAllCookies();
        testName = method.getName();
        TestClassName = getClass().getSimpleName();
        log("***********************" + testName + " has started executing" + "**************************");

    }

    //To verify  Adding a new movie
    @Test
    public void TC01_addANewMovie() {
        verifyTestIsExecutable(testName, TestClassName);
        MovieRentalService_LoginPage loginPage=new MovieRentalService_LoginPage(driver);
        loginPage.login_Admin();
        Admin_HomePage admin_homePage= new Admin_HomePage(driver);
        admin_homePage.addMovie(2);

    }


    //To verify Login as a user given by a data table
    @Test
    public void TC02_loginUser() {
        verifyTestIsExecutable(testName, TestClassName);
        MovieRentalService_LoginPage loginPage=new MovieRentalService_LoginPage(driver);
        loginPage.login_user();


    }
    @AfterMethod
    public void afterMethod(ITestResult result) {
        testCaseExecutionStatus(result, testName);
    }
    @AfterTest
    public void afterTest() {
        driver.quit();
    }
}
