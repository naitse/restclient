package org.example;

import org.example.model.UserData;
import org.example.model.UserResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static RestClient restClient;

    @BeforeAll
    public static void beforeAll(){
        restClient = new RestClient("https://reqres.in/");
    }

    @Test
    public void testApp() throws IOException, InterruptedException {

        UserResponse userResponse = restClient.get("api/users/2").validate()
                .code(200)
                .body("data.id", 2)
                .body("data.email", Condition.NOT_NULL)
                .body("data.juanico", Condition.NULL)
                .as(UserResponse.class);

        assert userResponse.getData().getId() == 2;

    }
}
