package com.library2.step_definitions;

import com.library2.pages.BookPage;
import com.library2.pages.DashboardPage;
import com.library2.pages.LoginPage;
import com.library2.utilities.DB_Utils;
import com.library2.utilities.Driver;
import com.library2.utilities.LibraryAPI_Utils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.List;
import java.util.Map;

public class StepDefs {

    RequestSpecification givenPart = RestAssured.given().log().uri();
    Response response;
    JsonPath jsonPath;
    ValidatableResponse thenPart;
    String pathParamValue;
    String id;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role) {
        String token = LibraryAPI_Utils.getTokenByRole(role);
        givenPart.header("x-library-token", token);
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(endpoint);                  // capture the response after sending the GET request
        thenPart = response.then();                                 // initialize thenPart for validations to avoid NullPointException
    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        thenPart.statusCode(expectedStatusCode);                    // validate status code
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String expectedContentType) {
        thenPart.contentType(expectedContentType);                  // validate contentType
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, Matchers.notNullValue());   // valid path != null
    }

    // -- us02 --

    @Given("Path param is {string}")
    public void path_param_is(String pathParamValue) {
        this.pathParamValue = pathParamValue;
        givenPart.pathParam("id", pathParamValue);
    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String key) {
        thenPart.body(key, Matchers.is(pathParamValue));
    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> paths) {

        for (String path : paths) {
            thenPart.body(path, Matchers.notNullValue());
        }

        /*
            Option #2:
            Assert.assertNotNull(jsonPath.getString(path));
         */
    }

    // -- us03 --

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String requestContentType) {
        givenPart.contentType(requestContentType);
    }

    Map<String, Object> randomDataMap;
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String randomData) {

       switch (randomData) {
           case "book":
               randomDataMap = LibraryAPI_Utils.getRandomBookMap();
               break;
           case "user":
               randomDataMap = LibraryAPI_Utils.getRandomUserMap();
               break;
           default:
               throw new RuntimeException("Wrong data type is provided");
       }

        // Add each key-value pair from the map as form parameters
        for (Map.Entry<String, Object> entry : randomDataMap.entrySet()) {      //entrySet() gives key-value pair from Map as a collection of Map.Entry objects
                                                                                //Map.entry -> represents one pair
            givenPart.formParam(entry.getKey(), entry.getValue());              //entry.key() -> gives key, entry.value -> gives value
                                                                                //this adds the key&value as a form param to the request
        }


        /*
        //chatGPT
//               String urlEncodedBody = randomDataMap.entrySet()
//                       .stream()
//                       .map(entry -> entry.getKey() + "=" + entry.getValue())
//                       .collect(Collectors.joining("&"));
//               givenPart.body(urlEncodedBody);

         */
    }

    LoginPage loginPage = new LoginPage();
    DashboardPage dashboardPage = new DashboardPage();

    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String role) {
        loginPage.login(role);
    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String specifiedPage) {
        dashboardPage.navigateToPage(specifiedPage);
    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = givenPart.when().post(endpoint);                  // capture the response after sending the GET request
        jsonPath = response.jsonPath();
        thenPart = response.then();                                  // initialize thenPart for validations to avoid NullPointException

        response.prettyPrint();
    }

    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String key, String expectedValue) {
        thenPart.body(key, Matchers.equalTo(expectedValue));

    }

    // -- us03 -- all layers

    Map<String, Object> expectedAPIData;
    Map<String, String> dbData;

    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {

        //1. Extract book_id from the API response
        id = jsonPath.getString("book_id");
        System.out.println("bookId = " + id);

        //2. API - get expected Data from API
        expectedAPIData = randomDataMap;

        //3. Database - run Query & store as DB Data
        String query = "select * from books where id = "+ id;
        DB_Utils.runQuery(query);
        dbData = DB_Utils.getRowMap(1);

        //4. UI - get data from UI
        String bookName = dbData.get("name");                            //using db to get the book name

        BookPage bookPage = new BookPage();
        bookPage.searchBox.sendKeys(bookName);                           //entered bookName from db data in the search box
        bookPage.searchBox.sendKeys(Keys.ENTER);

        String UIBookName = bookPage.bookNameUI(bookName).getText();     //stored the bookName from UI in a variable
        String UIIsbn = bookPage.bookISBN(dbData.get("isbn")).getText(); //stored the bookISBN from UI in a variable

        //5. Compare API, DB, UI data
        Assert.assertEquals(expectedAPIData.get("name"), dbData.get("name"));
        Assert.assertEquals(String.valueOf(expectedAPIData.get("year")), dbData.get("year"));

        Assert.assertEquals(expectedAPIData.get("name"), UIBookName);                        //compared API to UI bookName
        Assert.assertEquals(String.valueOf(expectedAPIData.get("isbn")), UIIsbn);           //compared API to UI ISBN

    }

    // -- us04 -- all layers

    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {

        //1. Extract user_id from the API response
        id = jsonPath.getString("user_id");
        System.out.println("userId = " + id);

        //2. API - get expected Data from API
        expectedAPIData = randomDataMap;

        //3. Database - run Query & store as DB Data
        String query = "select * from users where id = "+ id;
        DB_Utils.runQuery(query);
        dbData = DB_Utils.getRowMap(1);

        //4. Compare API with DB
        Assert.assertEquals(expectedAPIData.get("full_name"), dbData.get("full_name"));
        Assert.assertEquals(expectedAPIData.get("email"), dbData.get("email"));
        Assert.assertEquals(String.valueOf(expectedAPIData.get("user_group_id")), dbData.get("user_group_id"));

    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {

        String createdEmail = dbData.get("email");
        String createdPassword = (String) expectedAPIData.get("password");  //down-casting

        loginPage.login(createdEmail, createdPassword);

    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {

        String createdUserName = dbData.get("full_name");
        String createdUserNameUI = Driver.getDriver().findElement(By.xpath("//span[.=\""+createdUserName+"\"]")).getText();

        Assert.assertEquals(createdUserName, createdUserNameUI);

    }

    // -- us05 --

    String token;   //declare token globally to reuse

    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
        token = LibraryAPI_Utils.getToken(email,password);
        givenPart.header("x-library-token", token);
    }

    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        givenPart.formParam("token", token);        // Send token as part of the request body
    }





}
