package com.library2.pages;

import com.library2.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookPage extends BasePage {

    @FindBy(xpath = "//table/tbody/tr")
    public List<WebElement> allRows;

    @FindBy(xpath = "//input[@type='search']")
    public WebElement searchBox;

    @FindBy(id = "book_categories")
    public WebElement mainCategoryElement;

    @FindBy(css = ".dataTables_info")
    public WebElement bookCount;

    @FindBy(name = "name")
    public WebElement addBookName;

    @FindBy(xpath = "(//input[@type='text'])[4]")
    public WebElement author;

    @FindBy(name = "year")
    public WebElement addYear;

    @FindBy(name = "isbn")
    public WebElement addIsbn;

    @FindBy(id = "description")
    public WebElement addDescription;

    @FindBy(xpath = "//div[@class='portlet-title']//a")
    public WebElement addBook;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement saveChanges;

    @FindBy(xpath = "//div[@class='toast-message']")
    public WebElement toastMessage;

    @FindBy(id = "book_group_id")
    public WebElement categoryDropdown;


    public WebElement editBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

    public WebElement bookNameUI(String bookName){
        String xpath = "//td[3][.='" + bookName + "']";
        return Driver.getDriver().findElement(By.xpath(xpath));

    }

    public WebElement bookISBN(String bookISBN){
        String xpath = "//td[2][.='" + bookISBN + "']";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

   // Method to get the info from the UI
//    public Map<String, Object> getBookDataFromUI(String bookId){
//        Map<String, Object> bookDetails = new HashMap<>();
//
//        searchBox.sendKeys(bookId);
//
//    }

}
