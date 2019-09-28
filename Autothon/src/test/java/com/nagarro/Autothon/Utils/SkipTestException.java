package com.nagarro.Autothon.Utils;

import org.testng.SkipException;

public class SkipTestException extends SkipException {
    public SkipTestException(String message)
    {
        super(message);
    }


}
