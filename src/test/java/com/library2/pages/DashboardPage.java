package com.library2.pages;

import com.library2.utilities.Driver;
import com.library2.utilities.LibraryAPI_Utils;
import io.restassured.RestAssured;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage{

    @FindBy(id = "borrowed_books")
    public WebElement borrowedBooksNumber;

    @FindBy(id = "user_count")
    public WebElement usersNumber;

    @FindBy(id = "book_count")
    public WebElement booksNumber;






    public String getModuleCount(String module){

        //h6[normalize-space(.)='Users']//..//h2

        String locator="//h6[normalize-space(.)='"+module+"']//..//h2";

        WebElement elementOfModule = Driver.getDriver().findElement(By.xpath(locator));

        return elementOfModule.getText();
    }

    /*

    public void getDashboardStats(){

        RestAssured.given().log().uri()
            .header("x-library-token", token)
            .accept(ContentType.JSON)
            .when().get("/dashboard_stats").prettyPee()
            .then().statusCode(200);
    }

     */

}
