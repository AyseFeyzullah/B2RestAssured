package task;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import task.pojo.User;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TaskSolution {

    @Test
    public void task1() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }

    @Test
    public void task2() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))
        ;
    }

    @Test
    public void task3() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;
    }

    @Test
    public void task4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;
    }

    @Test
    public void task5() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userId[2]", equalTo(1))
                .body("title[2]", equalTo("fugiat veniam minus"))
        ;
    }


    @Test
    public void task6() {
        ToDo toDo =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(ToDo.class);

        System.out.println(toDo);
    }


    @Test
    public void task7_1() {
        List<ToDo> toDoList =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getList("", ToDo.class);

        System.out.println(toDoList);
    }


    @Test
    public void task7_2() {
        ToDo[] toDoArray =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(ToDo[].class);

        System.out.println(Arrays.toString(toDoArray));
    }

    @Test
    public void Test8(){
        Response response = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .contentType(ContentType.JSON)
                .extract().response();

        List<User> users = response.jsonPath().getList("", User.class);


    }



    @Test
    public void Test9(){
        int id = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .contentType(ContentType.JSON)
                .extract().path("find{it.name=='Ervin Howell'}.id");


        List<String> list = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/albums")
                .then()
                .contentType(ContentType.JSON)
                .extract().jsonPath().getList("findAll{it.userId==" + id + "}.title");

        for (String s : list) {
            System.out.println(s);
        }
        System.out.println(list.size());


    }


}
