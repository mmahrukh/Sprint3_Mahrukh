package com.library2.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LibraryAPI_Utils {

    /*
    public static String getToken(String userType){

        String email = ConfigurationReader.getProperty(userType + "_username");
        String password = ConfigurationReader.getProperty(userType + "_password");

        return getToken(email, password);
    }

     */

    public static String getToken(String email, String password) {

        JsonPath jsonPath = RestAssured.given().log().uri()
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)                 // Datatype I am sending to API
                .formParam("email", email)
                .formParam("password", password)
                .when().post("/login")
                .prettyPeek()
                .then().statusCode(200)
                //.contentType("application/json; charset=utf-8")
                .extract().jsonPath();

        String token = jsonPath.getString("token");

        return token;

    }


    public static String getTokenByRole(String role) {

        Map<String, String> roleCredentials = returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        return getToken(email, password);

    }

    public static Map<String, String> returnCredentials(String role) {
        String email = "";
        String password = "";

        switch (role) {
            case "librarian":
                email = System.getenv("LIBRARIAN_EMAIL");
                password = System.getenv("LIBRARIAN_PASSWORD");
                break;

            case "student":
                email = System.getenv("STUDENT_EMAIL");
                password = System.getenv("STUDENT_PASSWORD");
                break;

            default:
                throw new RuntimeException("Invalid Role Entry :\n>> " + role + " <<");
        }

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return credentials;

    }

    public static Map<String, Object> getRandomBookMap() {

        Faker faker = new Faker();
        Map<String, Object> bookMap = new LinkedHashMap<>();

        //create fake book data
        String randomBookName = "Mahh" + faker.name().name();
        String isbn = faker.code().isbn10();
        int year = faker.number().numberBetween(1990, 2025);
        String author = faker.book().author();
        int book_category_id = faker.number().numberBetween(0, 20);
        String description = faker.chuckNorris().fact();

        //add to my map
        bookMap.put("name", randomBookName);
        bookMap.put("isbn", isbn);
        bookMap.put("year", year);
        bookMap.put("author", author);
        bookMap.put("book_category_id", book_category_id);
        bookMap.put("description", description);

        return bookMap;
    }

    public static Map<String, Object> getRandomUserMap() {

        Faker faker = new Faker();
        Map<String, Object> userMap = new LinkedHashMap<>();

        // fake user data
        String fullName = faker.name().fullName();
        String email = fullName.substring(0, fullName.indexOf(" ")) + faker.number().numberBetween(0,10) + "@library";
        System.out.println("email = " + email);

        userMap.put("full_name", fullName);
        userMap.put("email", email);
        userMap.put("password", "libraryUser");

        //2 is librarian as role
        userMap.put("user_group_id", "2");
        userMap.put("status", "ACTIVE");
        userMap.put("start_date", "2023-03-11");
        userMap.put("end_date", "2024-03-11");
        userMap.put("address", faker.address().cityName());

        return userMap;
    }




}
