package com.nagarro.Autothon.PageObjects;

import com.nagarro.Autothon.GenericFunctions.BaseTestClass;
import com.nagarro.Autothon.GenericFunctions.URLProviderClass;
import com.nagarro.Autothon.GenericFunctions.Xls_Reader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class MovieRentalService_LoginPage {

    WebDriver driver;

    public MovieRentalService_LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    BaseTestClass base = new BaseTestClass();
    public static final String profile_Lnk = "(//a[@id='navLink'])[1]";
    public static final String login_Lnk = "(//a[@id='navLink'])[2]";
    public static final String signIn_Btn = "//button[@class='btn btn-success']";
    public static final String username_TextField = "//input[@name='username']";
    public static final String password_TextField = "//input[@name='password']";




    public void login_Admin()
    {
        base.log("Navigate to Movie Rental Service Page");
        driver.get(URLProviderClass.Return_MovieRentalServiceURL());
        Xls_Reader datatable = new Xls_Reader(base.getPropertyValue("autothon_TestAutomation"));
        String username = datatable.getCellData("MoieRentalService_Login", "Username", 2);
        String password = datatable.getCellData("MoieRentalService_Login", "Password", 2);
        base.log("Click on Login Button");
        driver.findElement(By.xpath(login_Lnk)).click();
        base.waitUntil(username_TextField,5);
        base.log("Fill Username");
        base.fill_Txt(username_TextField,password);
        base.log("Fill Password");
        base.fill_Txt(password_TextField,username);
        driver.findElement(By.xpath(signIn_Btn)).click();
        base.goToSleep(5000);
        base.log("Verify Log In");
        Assert.assertEquals(base.getElementText(Admin_HomePage.adminMenu_Txt),"Admin Menu","Login Failed");

    }


    public void login_user()
    {
        base.log("Navigate to Movie Rental Service Page");
        driver.get(URLProviderClass.Return_MovieRentalServiceURL());
        Xls_Reader datatable = new Xls_Reader(base.getPropertyValue("autothon_TestAutomation"));
        String username = datatable.getCellData("MoieRentalService_Login", "Username", 3);
        String password = datatable.getCellData("MoieRentalService_Login", "Password", 3);
        base.log("Click on Login Button");
        driver.findElement(By.xpath(login_Lnk)).click();
        base.waitUntil(username_TextField,5);
        base.log("Fill Username");
        base.fill_Txt(username_TextField,username);
        base.log("Fill Password");
        base.fill_Txt(password_TextField,password);
        driver.findElement(By.xpath(signIn_Btn)).click();
        base.log("Verify Log In");
    }










}
