package basics;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;


public class _01Basics {

    /*
        rest-assured BDD mantigina göre yazilim vardir
        given()   -> ön satlar
        when()    -> islemler
        then()    -> assertions

        RestAssured methodlarinin static importu gerekli

     */


    @Test
    public void text01_BasicUsage(){

        given()             // ön veriler, requirementler, headers, cookies, body,....
                .when()     // yapilan islem, GET, POST, DELETE, PUT
                .then();    // Assertions, statusCode, Json path assertions

        when()
                .get()
                .then();

    }

    @Test
    public void test02_get(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")  // GET methodu ile bu adrese request gönderdik
                .then()
                //.log().body()                                   // body verileri consola yazdirilir
                .log().all()
                //.log().cookies()
                //.log().headers()
                ;
    }


    @Test
    public void test03_statusCode(){
        String url = "https://reqres.in/api/users?page=2";
        given()
                .get(url)
                .then()
                .statusCode(200)        // statusCode 200 olmali
                ;
    }


    @Test
    public void test04_ResponseTime(){
        String url = "https://reqres.in/api/users?page=2";
        long time = given()
                .get(url)
                .timeIn(TimeUnit.MILLISECONDS)
        ;

        System.out.println(time);

    }


    @Test
    public void test05_pathParams(){
        given()
                .pathParams("page", 1)
                .get( "https://reqres.in/api/users?page={page}")
                .then()
                .log().body()
                .statusCode(200)        // statusCode 200 olmali
        ;
    }

    @Test
    public void test06_pathParams(){
        given()
                .pathParams("page", 1)
                .pathParams("link", "api")
                .get( "https://reqres.in/{link}/users?page={page}")
                .then()
                .log().body()
                .statusCode(200)        // statusCode 200 olmali
        ;
    }


    @Test
    public void test07_baseUri(){
        RestAssured.baseURI = "https://reqres.in";   // baseURL tanimi icindir

        given()
                .get( "https://reqres.in/api/users?page=1")
                .then()
                .statusCode(200)        // statusCode 200 olmali
        ;


        /*
            baseURI tanimli ise
                GET, POST, ... 'da http ya da https yoksa baseURI kullanilir
         */

        given()
                .get( "/api/users?page=1")
                .then()
                .statusCode(200)        // statusCode 200 olmali
        ;

    }

}
