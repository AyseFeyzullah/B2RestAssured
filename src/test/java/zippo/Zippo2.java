package zippo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import zippo.pojoClasses.Location;
import zippo.pojoClasses.Place;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Zippo2 {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
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
    public void test1_getData06068() {
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
    public void test2_getDataAndAssert() {
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
    public void test3_getDataAllStatesAreAnkara() {

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

    // TR ve 06080 yerine pathParam kullaniniz
    @Test
    public void test4_getDataUsePathParam() {

        String country = "TR";
        String postCode = "06080";

        given()
                .spec(requestSpecification)
                .pathParam("ulke", country)
                .pathParam("postaKodu", postCode)
                .when()
                .get("/{ulke}/{postaKodu}")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))
        ;
    }

    // country'yi extract edin ve Turkey oldugunu assert edin
    // 3. mahallenin adini extract edin ve Sokullu Mah. oldugunu assert edin
    @Test
    public void test5_getDataExtractPlaceName() {

        String country = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().path("country");
        Assert.assertEquals(country, "Turkey");

        String placeName = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                //.extract().path("places[2].'place name'")
                .extract().jsonPath().get("places[2].'place name'");
        Assert.assertEquals(placeName, "Sokullu Mah.");

    }

    @Test
    public void test6_getDataExtractPlaceName1() {

        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        String country = response.then().extract().path("country");
        Assert.assertEquals(country, "Turkey");

        String placeName = response.then().extract().jsonPath().get("places[2].'place name'");
        Assert.assertEquals(placeName, "Sokullu Mah.");

        response.prettyPrint();

    }


    // mahalle isimlerini liste olarak extract edin, size'inin 18 oldugunu assert edin
    @Test
    public void test7_getDataExtractPlaceNames() {

        Response res = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();


        //List<String> places = res.then().extract().path("places.'place name'");
        List<String> places = res.then().extract().jsonPath().getList("places.'place name'");

        Assert.assertEquals(18, places.size());

    }


    // /TR/06080 json datasini pojoya map adin
    @Test
    public void test8_getDataToPojo() {

        /*
                Location location = given()
                            .spec(requestSpecification)
                            .when()
                            .get("/TR/06080")
                            .then()
                            .spec(responseSpecification)
                            .extract().as(Location.class);

         */

        Response res = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                //.spec(responseSpecification)
                .extract().response();


        Location location = res.then().extract().as(Location.class);
        if (location.getPlaces() != null){
            for (Place place : location.getPlaces()) {
                String str = location.getCountry() + "\t" +
                        place.getState() + "\t" +
                        place.getPlaceName();

                System.out.println(str);
            }
        }

    }


    // Ankaranin tüm mahallelerini bulun

    @Test
    public void test9_getDataToPojo() throws IOException {

        FileWriter fileWriter = new FileWriter("Places.txt");
        for (int i = 6070; i < 6090; i++) {
            String postCode = getPostaKodu(i);
            Response response = given()
                    .spec(requestSpecification)
                    .pathParam("postaKodu", postCode)
                    .when()
                    .get("/TR/{postaKodu}")
                    .then()
                    .extract().response()
            ;

            Location location = response.then().extract().as(Location.class);
            if (location.getPlaces() != null){
                for (Place place : location.getPlaces()) {
                    String str = location.getCountry() + "\t" +
                            place.getState() + "\t" +
                            place.getPlaceName() + "\n";

                    fileWriter.write(str);
                }
            }


        }

        fileWriter.close();
    }

    public String getPostaKodu(int num){
        String code = String.valueOf(num);
        for (int i = code.length(); i < 5; i++) {
            code = "0".concat(code);
        }
        return code;
    }


    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2, 4, 6));

        int evenNumCount = (int) list.stream().filter(n -> n % 2 == 0).count();
        Assert.assertEquals(evenNumCount, list.size());

        int oddNumCount = (int) list.stream().filter(n -> n % 2 == 1).count();
        Assert.assertEquals(oddNumCount, 0);


        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("A", "abcd", "abc"));

        List<Integer> list1Nums = list1.stream().map(String::length).collect(Collectors.toList());
        //Integer a = (Integer) list1Nums.stream().max(Integer::compareTo);
        //System.out.println(list1.get(list1Nums.indexOf()));
    }
}
