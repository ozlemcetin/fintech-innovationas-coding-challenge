package io.bankbridge.api;

import com.google.gson.Gson;
import io.bankbridge.model.BankModel;
import io.bankbridge.response.StatusResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BankCRUDAPIIntegrationTest {

    private static final int DEFAULT_PORT = BankCRUDAPI.DEFAULT_PORT;


    @org.junit.jupiter.api.BeforeAll
    public static void setUp() {
        //call main()
        BankCRUDAPI.main(null);
    }


    @org.junit.jupiter.api.AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    @org.junit.jupiter.api.Test
    void postBankJson() throws IOException {

        /*
         POST http://localhost:4567/bank/json
            {
                "bic": "1012",
                "name": "Turkiye Is Bankasi",
                "countryCode": "TR",
                "auth": "OAUTH"
            }
         */
        String jsonInputString = new Gson()
                .toJson(new BankModel("1012", "Turkiye Is Bankasi", "TR", "OAUTH"));

        //when
        String path = "/bank/json";
        HttpURLConnectionResponse response = HttpURLConnectionResponse
                .createURLAndConnect(DEFAULT_PORT,
                        path, HttpMethod.POST, jsonInputString);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.CREATED_201, response.getResponseCode());

    }

    @org.junit.jupiter.api.Test
    void postBankQueryParams() throws IOException {

        //when
        String path = "/bank/queryParams?bic=1012&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
        HttpURLConnectionResponse response = HttpURLConnectionResponse
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.POST, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.CREATED_201, response.getResponseCode());

    }

    @org.junit.jupiter.api.Test
    void getBanks() throws IOException {

        //given
        String bic = "1012";
        String name = "ING";
        {
            String path1 = "/bank/queryParams?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);

            String path2 = "/bank/queryParams?bic=1932&name=" + name + "&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path2, HttpMethod.POST, null);
        }

        //when
        String path = "/banks";
        HttpURLConnectionResponse response = HttpURLConnectionResponse
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);


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
            String path = "/bank/4573";
            HttpURLConnectionResponse response = HttpURLConnectionResponse
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);
        });
    }

    @org.junit.jupiter.api.Test
    void getBankFound() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = "/bank/queryParams?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }


        //when
        String path = "/bank/" + bic;
        HttpURLConnectionResponse response = HttpURLConnectionResponse
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
            String path1 = "/bank/queryParams?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        String name = "ING";
        String country = "UK";
        String auth = "SSL";
        String jsonInputString = new Gson()
                .toJson(new BankModel(bic, name, country, auth));

        //when
        String path = "/bank/json/" + bic;
        HttpURLConnectionResponse response = HttpURLConnectionResponse
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
            HttpURLConnectionResponse response = HttpURLConnectionResponse
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);
        });
    }

    @org.junit.jupiter.api.Test
    void putBankQueryParamsFound() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = "/bank/queryParams?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        String name = "ING";
        String country = "UK";
        String auth = "SSL";

        //when
        String path = "/bank/queryParams/" + bic + "?name=" + name + "&countryCode=" + country + "&auth=" + auth;
        HttpURLConnectionResponse response =
                HttpURLConnectionResponse.createURLAndConnect(
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
            HttpURLConnectionResponse response = HttpURLConnectionResponse
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.GET, null);
        });
    }

    @org.junit.jupiter.api.Test
    void deleteBank() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = "/bank/queryParams?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        //when
        String path = "/bank/" + bic;
        HttpURLConnectionResponse response = HttpURLConnectionResponse
                .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.DELETE, null);


        //assertions
        assertNotNull(response);
        assertNotNull(response.getInputStream());
        assertTrue(response.getInputStream().contains(StatusResponse.SUCCESS.toString()));
        assertEquals(HttpStatus.OK_200, response.getResponseCode());

    }

    @org.junit.jupiter.api.Test
    void optionsBankExists() throws IOException {

        //given
        String bic = "1012";
        {
            String path1 = "/bank/queryParams?bic=" + bic + "&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL";
            HttpURLConnectionResponse.createURLAndConnect(
                    DEFAULT_PORT, path1, HttpMethod.POST, null);
        }

        //when
        String path = "/bank/" + bic;
        HttpURLConnectionResponse response = HttpURLConnectionResponse
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
            HttpURLConnectionResponse response = HttpURLConnectionResponse
                    .createURLAndConnect(DEFAULT_PORT, path, HttpMethod.OPTIONS, null);
        });
    }


}