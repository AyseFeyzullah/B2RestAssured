package localJsonServer;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Odev {

    @BeforeTest
    public void beforeTest(){
        RestAssured.baseURI = "http://localhost:3000";
    }


    @Test(invocationCount = 10)
    public void test1(){
        // 10 adet user kaydi yapacak
    }


    @Test(priority = 1, dependsOnMethods = "test1")
    public void test2(){
        // tüm username'ler icindeki en uzun username'i ekrana yazdiracak
    }

}
