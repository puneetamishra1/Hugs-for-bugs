package com.nagarro.Autothon.PageObjects;

import com.nagarro.Autothon.GenericFunctions.BaseTestClass;
import com.nagarro.Autothon.GenericFunctions.Xls_Reader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Admin_HomePage {

    WebDriver driver;

    public Admin_HomePage(WebDriver driver) {
        this.driver = driver;
    }

    BaseTestClass base = new BaseTestClass();
    public static final String adminMenu_Txt = "//span[text()='Admin Menu']";
    public static final String addMovie_Lnk = "//a[@class='navLink']";
    public static final String title_TextField = "//button[@class='btn btn-success']";
    public static final String director_TextField = "//button[@class='btn btn-success']";
    public static final String description_TextField = "//button[@class='btn btn-success']";
    public static final String url_TextField = "//button[@class='btn btn-success']";
    public static final String saveMovie_Btn = "//button[@class='btn btn-success']";
    public static final String categories_DD = "//select[@name='categories']";
    public static final String categories_DDValues = "//select[@name='categories']/option";




    public void addMovie(int row)
    {
        Xls_Reader datatable = new Xls_Reader(base.getPropertyValue("autothon_TestAutomation"));
        base.log("Click on Add movie link");
        driver.findElement(By.xpath(addMovie_Lnk)).click();
        base.log("Fill Title Details");
        String title = datatable.getCellData("Add Movie Description", "Title", row);
        base.fill_Txt(title_TextField,title);
        base.log("Fill Director Details");
        String director = datatable.getCellData("Add Movie Description", "Director", row);
        base.fill_Txt(director_TextField,director);
        base.log("Fill Description Details");
        String description = datatable.getCellData("Add Movie Description", "Description", row);
        base.fill_Txt(description_TextField,description);
        base.log("Enter Category");
        String categories = datatable.getCellData("Add Movie Description", "Categories", row);
        base.select_DropDown(categories_DD,categories_DDValues,categories);
        base.log("Fill URL Details");
        String url = datatable.getCellData("Add Movie Description", "URL", row);
        base.fill_Txt(url_TextField,url);
        String rating = datatable.getCellData("Add Movie Description", "Rating", row);
        base.log("Select Rating");
        driver.findElement(By.xpath(saveMovie_Btn)).click();

    }
}
