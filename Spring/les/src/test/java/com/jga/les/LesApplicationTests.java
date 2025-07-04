package com.jga.les;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LesApplicationTests {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;

        // Faz login e obtém o token
        Map<String, String> login = new HashMap<>();
        login.put("login", "admin");
        login.put("senha", "admin");

        Response response = given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("/usuario/login");

        response.then().statusCode(200);
        token = response.jsonPath().getString("token");
    }

    @Test
    public void testInserirFornecedor() {
        Map<String, Object> fornecedor = new HashMap<>();
        fornecedor.put("nome", "Fornecedor Teste");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body(fornecedor)
        .when()
            .post("/fornecedor/add")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("nome", equalTo("Fornecedor Teste"));
    }

    @Test
    public void testListarFornecedores() {
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/fornecedor/list")
            .then()
            .statusCode(200)
            .body("$", not(empty())); // espera-se que pelo menos 1 fornecedor exista
    }
}
