package io.bankbridge.controller;

import io.bankbridge.api.BankListAPI;
import io.bankbridge.util.HttpURLConnectionUtil;
import io.bankbridge.util.Paths;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankListControllerIT {

    private static final int DEFAULT_PORT = BankListAPI.DEFAULT_PORT;

    @BeforeAll
    public static void setUp() throws Exception {
        //call main()
        BankListAPI.main(null);
    }


    @AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    void getV1BanksAll() throws IOException {

        //when
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT,
                        Paths.API_V1_BANKS_ALL, HttpMethod.GET, null);


        /*
        contains banks-v1.json id/name values
         */
        String bic = "1234";
        String name = "Royal Bank of Boredom";


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(bic));
        assertTrue(response.getInputStream().contains(name));


    }

    @Test
    void getV2BanksAll() throws IOException {


        //when
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT,
                        Paths.API_V2_BANKS_ALL, HttpMethod.GET, null);

        /*
        contains remote calls data
         */

        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(RemoteCallsController.EXAMPLE_NAME));
        assertTrue(response.getInputStream().contains(RemoteCallsController.EXAMPLE_BIC));
        assertTrue(response.getInputStream().contains(RemoteCallsController.EXAMPLE_AUTH));

    }


}