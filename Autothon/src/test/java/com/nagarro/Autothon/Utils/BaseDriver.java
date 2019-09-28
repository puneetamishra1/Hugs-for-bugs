package com.nagarro.Autothon.Utils;

import org.openqa.selenium.WebDriver;

public class BaseDriver {

	//This class comprises methods for getting and setting Webdriver
	public static WebDriver driver;

	public static WebDriver getDriver()
	{
		return driver;
	}

	public static void setDriver(WebDriver driver)
	{
		BaseDriver.driver = driver;
	}
}
