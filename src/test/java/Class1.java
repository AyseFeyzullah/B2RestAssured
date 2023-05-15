import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static io.restassured.RestAssured.given;

public class Class1 {


    @Test
    public void updateBook() {
        RestAssured.baseURI = "https://reqres.in/";
        RequestSpecification request = given();
        Response response = request.get("/api/users/2");
        response.prettyPrint();

    }


}


