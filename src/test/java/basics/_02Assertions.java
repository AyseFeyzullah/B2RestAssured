package basics;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _02Assertions {

    @BeforeTest
    public void beforeTest(){
        RestAssured.baseURI = "https://reqres.in";
    }


    @Test
    public void test01(){
        given()
                .pathParams("id", 2)
                .when()
                .get("/api/users/{id}")
                .then()
                .log().body()
                // statusCode 200 olmali
                .statusCode(200)

                // data'in icindeki first_name Janet olmali
                .body("data.first_name", equalTo("Janet"))

                // data'in icindeki last_name Weaver olmali
                .body("data.last_name", equalTo("Weaver"))

                // data key olarak id'si olmali
                .body("data", hasKey("id"))

                // support'un icindeki key olarak urlolmali
                .body("support", hasKey("url"))

                // json "contributions" kelimesini icermeli
                .body(containsString("contributions"))

                //data'nin id'si 2 ya da daha kücük olmali
                .body("data.id", lessThanOrEqualTo(2))
                ;
    }


    @Test(dataProvider = "getIds")
    public void test02_dataProvider(int id){
        given()
                .pathParams("id", id)
                .when()
                .get("/api/users/{id}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }


    @DataProvider
    public Object[][] getIds(){
        return new Object[][]{
                {1},
                {2},
                {3}
        };
    }
}
