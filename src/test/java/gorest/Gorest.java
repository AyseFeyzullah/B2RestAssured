package gorest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

public class Gorest {


    // Test 1: getAll users
    /*
        curl -i -H "Accept:application/json"
                -H "Content-Type:application/json"
                -H "Authorization: Bearer ACCESS-TOKEN"
                -XGET "https://gorest.co.in/public/v2/users"
     */

    @Test
    public void test1_getAllUsers() {
        given()
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                .log().body()
                .statusCode(oneOf(200, 201, 204))   // statusCode 200 ya da 201 ya da 204 olmali
        ;
    }


    // listelenen userlardan ilk name'i degiskene kaydedin
    @Test
    public void test2_getFirstName() {
        String obj = given()
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                //.log().body()
                .statusCode(oneOf(200, 201, 204))   // statusCode 200 ya da 201 ya da 204 olmali
                .extract().path("name[0]")     // name arrayinin ilk elemani extract eder
                ;
        System.out.println(obj);

    }

    // /users  ile gelen tüm isimleri list'e atin, siralayin ve konsola yazdirin

    @Test
    public void test3_getAllNamesThenSortThenWriteToConsol() {
        List<String> list = given()
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                //.log().body()
                .statusCode(oneOf(200, 201, 204))   // statusCode 200 ya da 201 ya da 204 olmali
                .extract().path("name")     // name arrayinin ilk elemani extract eder
                ;

        Collections.sort(list);
        System.out.println(list);
    }

    // /users  ile gelen sadece female isimleri list'e atin, siralayin ve konsola yazdirin
    @Test
    public void test3_getAllFemaleNamesThenSortThenWriteToConsol() {
        List<String> list = given()
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                //.log().body()
                .body(not(empty()))
                .statusCode(oneOf(200, 201, 204))
                .extract().path("findAll{it.gender=='female'}.name");

        Collections.sort(list);
        System.out.println(list);
        /*
            findAll{it.gender=='female'}.name
            array icinde gender=='female' olanlarin name'lerini return eder

            users.findAll{it.gender=='male'}.name
            json icindeki users arrayinin altinda gender=='male' olanlarin name.lerini return erder

            users.name -> users icindeki name'lerin olusturdugu array
            users.name[0] -> users icindeki name'lerin olusturdugu arrayin ilk elemani

         */
    }

    @Test
    public void test3_getAllNameWithJsonPath() {

        List<String> list = given()
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                //.log().body()
                .body(not(empty()))
                .statusCode(oneOf(200, 201, 204))
                .extract().jsonPath().getList("name")
                //.extract().jsonPath().get("name[0]")
                ;

        Collections.sort(list);
        System.out.println(list);

    }


    //Specifications -> spec
    @Test
    public void test4_genel() {

        // RestAssured.baseURI = "";
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                //.addHeader("Authentication", "Bearer XXXXXXXXXX")
                .setBaseUri("https://gorest.co.in")
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .build();


        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users")
                .then()
                .spec(resSpec)
        ;
        /*
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(oneOf(200, 201, 204))
                .contentType(ContentType.JSON)
        ;

         */


    }

    @Test
    public void getAUser(){
        get("https://gorest.co.in/public/v2/users/1049")
                .then()
                .statusCode(200)
                .log().body()
                ;
    }


    @Test
    public void getAUserInAClass(){
        User user = get("https://gorest.co.in/public/v2/users/1049")
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(User.class)
        ;

        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
    }


    /*
        Yeni Bir class acin
        createUser'i map ile create edin
        id'yi alin
        kaydedile user'i get yapip Uer class'ina map edin.
        User nesnesinin field'larini sout ile yazdirin

     */


    @Test
    public void getAllUserInAClass(){
        List<User> users = get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(200)
                .log().body()
                .extract().jsonPath().getList("", User.class)
                ;


        for (User user : users) {
            System.out.println(user);
            System.out.println("---------------");

        }

    }

    @Test
    public void getReponse(){
        Response response = get("https://gorest.co.in/public/v2/users/1049")
                .then().extract().response();

        String name = response.path("name");
        String email = response.jsonPath().getString("email");

        System.out.println("name = " + name);
        System.out.println("email = " + email);
    }

    @Test
    public void getReponse1(){
        String response = get("https://gorest.co.in/public/v2/users/1049")
                .asString();

        String name = from(response).get("name");
        String email = from(response).get("email");

        System.out.println("name = " + name);
        System.out.println("email = " + email);

    }


}




/*
    json asagidaki gibi ise :

    [
        {
            "id": 1588988,
            "name": "Trilokesh Gupta",
            "email": "trilokesh_gupta@wunsch.test",
            "gender": "female",
            "status": "active"
        },
        {
            "id": 1588987,
            "name": "Datta Gill",
            "email": "datta_gill@prohaska-mosciski.test",
            "gender": "male",
            "status": "inactive"
        }
    ]

    body array olarak return edilmis
    .extract().jsonPath().getList("", User.class)
    .extract().jsonPath().getList("$", User.class)

    "", "$" : jsonBody anlamina gelir






     json asagidaki gibi ise :

    {
        "type" : "user",
        "users":[
            {
                "id": 1588988,
                "name": "Trilokesh Gupta",
                "email": "trilokesh_gupta@wunsch.test",
                "gender": "female",
                "status": "active"
            },
            {
                "id": 1588987,
                "name": "Datta Gill",
                "email": "datta_gill@prohaska-mosciski.test",
                "gender": "male",
                "status": "inactive"
            }
        ]
    }

    body object olarak return edilmis
    List<Users> listOfUser = given()....extract().jsonPath().getList("users", User.class)
    Main main = given().....extract().as(Main.class)

    class Main{
        String type;
        ArrayList<User> users;
    }

    class User{
        int id;
        String name;
        String email;
        String gender;
        String status;
    }
}


 */


