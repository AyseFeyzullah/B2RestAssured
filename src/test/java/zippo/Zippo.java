package zippo;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

public class Zippo {


    @Test
    public void test1(){
        // https://api.zippopotam.us/us/90210
        get("https://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .log().body()
                .body("country", equalTo("United States"))
                .body("places[0].state", equalTo("California"))
                .body("'country abbreviation'", equalTo("US"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))


        ;

    }


    @Test
    public void test2(){

        get("https://api.zippopotam.us/TR/34200")
                .then()
                .statusCode(200)
                .log().body()


        ;

    }
}
