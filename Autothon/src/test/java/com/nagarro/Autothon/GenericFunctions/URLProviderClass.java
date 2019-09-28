package com.nagarro.Autothon.GenericFunctions;

public class URLProviderClass  extends  BaseTestClass{
    BaseTestClass base = new BaseTestClass();

    public static String environment = BaseTestClass.environment;


    public static String Return_MovieRentalServiceURL() {
        System.out.print(environment);
        if (environment.toLowerCase().contains("local"))
            return "https://autothon-nagarro-frontend-y03.azurewebsites.net/";
        else
            return "https://autothon-nagarro-frontend-y03.azurewebsites.net/";
    }


}
