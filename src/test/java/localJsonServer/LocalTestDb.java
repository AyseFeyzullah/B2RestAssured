package localJsonServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class LocalTestDb {

    @BeforeTest
    public void beforeTest(){
        RestAssured.baseURI = "http://localhost:3000";
    }


    @Test
    public void test01_getAllUsers(){
        given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
        ;

        // hasItem -> tekdeger assert
        // hasItems -> arary icinde olmasi beklenen degerler
    }

    @Test
    public void test01_getAllUsers1(){
        given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", hasItems(1,2))  // assert
                ;

        // hasItem -> tekdeger assert
        // hasItems -> array icinde olmasi beklenen degerler
    }



    // extract, a value
    @Test
    public void test01_getAllUsers_extractData(){
        int id = given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id[0]")          // extract response'un icindeki veriye ulasmamiz icin
                                                    // path itedigimiz verinin yolunu veririz
        ;

        System.out.println("id[0] : " + id);

    }

    // extract, a value
    @Test
    public void test01_getAllUsers_extractList(){
        List<Integer> list = given()
                .when()
                .get("/users")
                .then()
                //.log().all()
                .statusCode(200)
                .extract().path("id")
                ;

        System.out.println("ids : " + list);

    }


    @Test
    public void postdata(){

        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String password = RandomStringUtils.randomAlphabetic(5,10);
        String username = RandomStringUtils.randomAlphabetic(5,10);

        String json = "{\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"username\": \"" + username + "\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)      // body, request (giden) contentType
                .body(json)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)  // body, response (gelen) contentType, assert
                ;

        /*
        given()
                .when()
                .get("/users")
                .then()
                .log().body()
                ;

         */



    }


    public static void main(String[] args) {
        System.out.println(getRandomString(20, 30));
    }

    // getRandomString(3, 5) -> Ab5c
    public static String getRandomString(int min, int max){
        String str = "abcdefABCDEF012345 ";

        String rndStr = "";

        int last = min + new Random().nextInt(max-min);
        for (int i = 0; i < last; i++) {
            rndStr += str.charAt(new Random().nextInt(str.length()));
        }
        return rndStr;
    }




    @Test
    public void postdata2(){

        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String password = RandomStringUtils.randomAlphabetic(5,10);
        String username = RandomStringUtils.randomAlphabetic(5,10);

        String json = "{\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"username\": \"" + username + "\"\n" +
                "}";

        int id = given()
                .contentType(ContentType.JSON)      // body, request (giden) contentType
                .body(json)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;
        System.out.println("Kayit id : " + id);




        String jsonForUpdate = "{\n" +
                "    \"email\": \"" + RandomStringUtils.randomAlphabetic(10,20) + "@mail.net\",\n" +
                "    \"password\": \"" + RandomStringUtils.randomAlphabetic(5,10) + "\",\n" +
                "    \"username\": \"" + RandomStringUtils.randomAlphabetic(5,10) + "\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(jsonForUpdate)
                .when()
                .put("/users/" + id)
                .then()
                .log().body()
                .statusCode(200)
                ;

        given()
                .when()
                .delete("/users/" + id)
                .then()
                .statusCode(200)
        ;



    }


    /*
        Yeni bir class'da
        Test1 : post ile random 10 kayit ekleyin
        Test2 : users'larin username'leri liste olarak alin
                max uzunluktaki username'i ekrana yazdirin


     */

}
