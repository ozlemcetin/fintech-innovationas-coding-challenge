package io.bankbridge.controller;

import com.google.gson.Gson;
import io.bankbridge.api.BankCRUDAPI;
import io.bankbridge.model.BankModel;
import io.bankbridge.response.StatusResponse;
import io.bankbridge.util.HttpURLConnectionUtil;
import io.bankbridge.util.Paths;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BankCRUDControllerIT {

    private static final int DEFAULT_PORT = BankCRUDAPI.DEFAULT_PORT;

    @BeforeAll
    public static void setUp() {
        //call main()
        BankCRUDAPI.main(null);
    }


    @AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    void postBankJson() throws IOException {

        String jsonInputString = new Gson()
                .toJson(new BankModel("1012", "Turkiye Is Bankasi", "TR", "OAUTH"));

        //when
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT,
                        Paths.API_BANK_JSON, HttpMethod.POST, jsonInputString);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.CREATED_201, response.getResponseCode());

    }

    @Test
    void postBankQueryParams() throws IOException {

        //when
        String path = Paths.API_BANK_QUERY_PARAMS + "?bic=1012&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.POST, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.CREATED_201, response.getResponseCode());

    }

    @Test
    void getBanks() throws IOException {

        //given
        String bic = "1012";
        String name = "ING";
        {
            String path1 = Paths.API_BANK_QUERY_PARAMS
                    + "?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);

            String path2 = Paths.API_BANK_QUERY_PARAMS
                    + "?bic=1932&name=" + name + "&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path2, HttpMethod.POST, null);
        }

        //when
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT, Paths.API_BANKS, HttpMethod.GET, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertTrue(response.getInputStream().contains(bic));
        assertTrue(response.getInputStream().contains(name));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

    }

    @Test
    public void getBankNotFound() throws IOException {

        Assertions.assertThrows(FileNotFoundException.class, () -> {

            //when
            String path = "/bank/4573/";
            HttpURLConnectionUtil response = HttpURLConnectionUtil
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);
        });
    }

    @Test
    void getBankFound() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = Paths.API_BANK_QUERY_PARAMS +
                    "?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }


        //when
        String path = "/bank/" + bic;
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertTrue(response.getInputStream().contains(bic));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

    }

    @org.junit.jupiter.api.Test
    void putBankJsonFound() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = Paths.API_BANK_QUERY_PARAMS +
                    "?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        String name = "ING";
        String country = "UK";
        String auth = "SSL";
        String jsonInputString = new Gson()
                .toJson(new BankModel(bic, name, country, auth));

        //when
        String path = "/bank/json/" + bic;
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.PUT, jsonInputString);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertTrue(response.getInputStream().contains(bic));
        assertTrue(response.getInputStream().contains(name));
        assertTrue(response.getInputStream().contains(country));
        assertTrue(response.getInputStream().contains(auth));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

    }

    @Test
    public void putBankJsonNotFound() throws IOException {

        Assertions.assertThrows(FileNotFoundException.class, () -> {

            //when
            String path = "/bank/json/1012";
            HttpURLConnectionUtil response = HttpURLConnectionUtil
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);
        });
    }

    @Test
    void putBankQueryParamsFound() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = Paths.API_BANK_QUERY_PARAMS +
                    "?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        String name = "ING";
        String country = "UK";
        String auth = "SSL";

        //when
        String path = "/bank/queryParams/" + bic + "/?name=" + name + "&countryCode=" + country + "&auth=" + auth;
        HttpURLConnectionUtil response =
                HttpURLConnectionUtil.createURLAndConnect(
                        DEFAULT_PORT, path, HttpMethod.PUT, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertTrue(response.getInputStream().contains(bic));
        assertTrue(response.getInputStream().contains(name));
        assertTrue(response.getInputStream().contains(country));
        assertTrue(response.getInputStream().contains(auth));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

    }

    @Test
    public void putBankQueryParamsNotFound() throws IOException {

        Assertions.assertThrows(FileNotFoundException.class, () -> {

            //when
            String path = "/bank/queryParams/1012";
            HttpURLConnectionUtil response = HttpURLConnectionUtil
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);
        });
    }

    @Test
    void deleteBank() throws IOException {

        //given
        String bic = "9995";
        {
            String path1 = Paths.API_BANK_QUERY_PARAMS +
                    "?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        //when
        String path = "/bank/" + bic + "/delete/";
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.DELETE, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

        //Bank Not Found
        {
            Assertions.assertThrows(FileNotFoundException.class, () -> {

                //when
                String path2 = "/bank/queryParams/" + bic;
                HttpURLConnectionUtil response2 = HttpURLConnectionUtil
                        .createURLAndConnect(DEFAULT_PORT, path2, HttpMethod.GET, null);
            });
        }

    }

    @Test
    void optionsBankExists() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = Paths.API_BANK_QUERY_PARAMS +
                    "?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionUtil.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        //when
        String path = "/bank/" + bic + "/options/";
        HttpURLConnectionUtil response = HttpURLConnectionUtil
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.OPTIONS, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

        //"Bank Exists"
        assertTrue(response.getInputStream().contains("Bank Exists"));

    }

    @Test
    public void optionsBankNotExists() throws IOException {

        Assertions.assertThrows(FileNotFoundException.class, () -> {

            //when
            String path = "/bank/9967";
            HttpURLConnectionUtil response = HttpURLConnectionUtil
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.OPTIONS, null);
        });
    }


}