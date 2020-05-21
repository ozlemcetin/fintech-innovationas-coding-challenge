package io.bankbridge.api;

import io.bankbridge.response.StatusResponse;
import org.eclipse.jetty.http.HttpMethod;
import spark.Spark;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankListAPIIntegrationTest {


    private static final int DEFAULT_PORT = BankListAPI.DEFAULT_PORT;


    @org.junit.jupiter.api.BeforeAll
    public static void setUp() throws Exception {
        //call main()
        BankListAPI.main(null);
    }


    @org.junit.jupiter.api.AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    @org.junit.jupiter.api.Test
    void getV1BanksAll() throws IOException {

        //when
        String path = "/v1/banks/all";
        HttpURLConnectionResponse response = HttpURLConnectionResponse
                .createURLAndConnect(DEFAULT_PORT,
                        path, HttpMethod.GET, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));

    }

    @org.junit.jupiter.api.Test
    void getV2BanksAll() throws IOException {

        //when
        String path = "/v2/banks/all";
        HttpURLConnectionResponse response = HttpURLConnectionResponse
                .createURLAndConnect(DEFAULT_PORT,
                        path, HttpMethod.GET, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));

    }


}