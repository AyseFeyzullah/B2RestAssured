package zippo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Zippo2 {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = "https://api.zippopotam.us/";

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.zippopotam.us/")
                //.setBasePath("/TR/")
                .log(LogDetail.URI)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }



    // 1.  https://api.zippopotam.us/tr/06080 datasini get edin
    @Test
    public void test1_getData06068(){
        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                ;
    }


    // post code'un empty olmadigini
    // country'nin Turkey oldugunu
    // country abbreviation'in TR oldugunu
    // Places'in 3. elemaninin place name'inin  Sokullu Mah. oldugunu
    // places'in size'inin 18 oldugunu matcher ile assert edin
    @Test
    public void test2_getDataAndAssert(){
        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .body("'post code'", not(empty()))  // post code not empty oldugunu
                .body("country", equalTo("Turkey")) // country'nin Turkey oldugunu
                .body("'country abbreviation'", equalTo("TR"))  // country abbreviation'in TR oldugunu
                .body("places[2].'place name'", equalTo("Sokullu Mah."))
                .body("places", hasSize(18))
        ;
    }


    // places'larda tüm state'lerin Ankara oldugunu assert edin
    @Test
    public void test2_getDataAllStatesAreAnkara(){

        // json'daki places'in size'i 18 dir

        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))
        ;
    }

    // places'larda tüm state'lerin Ankara oldugunu assert edin
    @Test
    public void test2_getDataUsePathParam(){

        // json'daki places'in size'i 18 dir

        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))
        ;
    }








    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2,4,6));

        int evenNumCount = (int) list.stream().filter(n -> n % 2 == 0).count();
        Assert.assertEquals(evenNumCount,list.size());

        int oddNumCount = (int) list.stream().filter(n -> n % 2 == 1).count();
        Assert.assertEquals(oddNumCount, 0);
    }
}
