package com.library2.pages;

import com.library2.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    public BasePage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[@href='#dashboard']")
    public WebElement dashboard;

    @FindBy(xpath = "//a[@href='#users']")
    public WebElement users;

    @FindBy(xpath = "//a[@href='#books']")
    public WebElement books;

    @FindBy(xpath = "//a[@id='navbarDropdown']")
    public WebElement accountHolderName;

    @FindBy(xpath = "//a[.='Log Out']")
    public WebElement logOutButton;

    public void logOut(){
        accountHolderName.click();
        logOutButton.click();
    }

    public void navigateToPage(String page){

        if (page.equalsIgnoreCase("dashboard")) {
            this.dashboard.click();
        } else if (page.equalsIgnoreCase("users")) {
            this.users.click();
        } else {
            this.books.click();
        }

    }

}
