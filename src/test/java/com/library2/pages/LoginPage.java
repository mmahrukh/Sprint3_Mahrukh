package com.library2.pages;

import com.library2.utilities.ConfigurationReader;
import com.library2.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class LoginPage{

    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//input[@type='email']")
    public WebElement emailInput;

    @FindBy(xpath = "//input[@type='password']")
    public WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement signInButton;


    public void login(String email, String password){
        this.emailInput.sendKeys(email);
        this.passwordInput.sendKeys(password);
        this.signInButton.click();
    }

    //login by role using API Util method
    public void login(String role){

        /*
        //Get Credentials -> email & password
        Map<String, String> roleCredentials = LibraryAPI_Utils.returnCredentials(role);

        //From the Map, get individual fields
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        //login
        login(email, password);
         */

        this.emailInput.sendKeys(ConfigurationReader.getProperty(role + "_username"));
        this.passwordInput.sendKeys(ConfigurationReader.getProperty(role + "_password"));
        this.signInButton.click();

    }

    /*

    //login by role if we have scenario outline
    public void loginMethod(String userType) {
        this.emailInput.sendKeys(ConfigurationReader.getProperty(userType+"_username"));
        this.passwordInput.sendKeys(ConfigurationReader.getProperty(userType+"_password"));
        this.signInButton.click();
    }

     */


}
