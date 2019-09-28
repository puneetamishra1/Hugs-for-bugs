package com.nagarro.Autothon.GenericFunctions;

import org.testng.annotations.DataProvider;

public class DataProviders {

    BaseTestClass base = new BaseTestClass();

    Xls_Reader datatable = new Xls_Reader(base.getPropertyValue("autothon_TestAutomation"));



    @DataProvider(name = "Header")
    public Object[][] Supplier_header() {
        int rowCount = datatable.getRowCount("Retailer_Header");
        Object[][] result = new Object[rowCount - 1][2];
        for (int i = 0; i < rowCount - 1; i++)
        {           result[i][0] = i;
            result[i][1] = datatable.getCellData("Retailer_Header", "All_Products", i + 2);
        }
        return result;
    }
    @DataProvider(name = "New Password")
    public Object[][] Retailers_NewPassword() {
        int rowCount = datatable.getRowCount("RetailersResetPassword");
        Object[][] result = new Object[rowCount - 1][3];
        for (int i = 0; i < rowCount - 1; i++)
        {           result[i][0] = i;
            result[i][1] = datatable.getCellData("RetailersResetPassword", "Data", i + 2);
            result[i][2] = datatable.getCellData("RetailersResetPassword", "ErrorMessage", i + 2);
        }
        return result;
    }


}
