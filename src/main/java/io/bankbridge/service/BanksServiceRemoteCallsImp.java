package io.bankbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.bankbridge.model.BankModel;
import org.eclipse.jetty.http.HttpMethod;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BanksServiceRemoteCallsImp implements BanksService {

    private static final String FILE_NAME = "banks-v2.json";

    //Public as being used in test scenarios
    public static Map<String, String> config;

    public static void init() throws Exception {

        //get the config values from file
        config = new ObjectMapper()
                .readValue(Thread.currentThread().getContextClassLoader().getResource(FILE_NAME), Map.class);
    }

    public String getBanks() {

        try {

            //initialize
            init();

            List<BankModel> banks = new ArrayList<>();
            for (String key : config.keySet()) {

                String url = config.get(key);

                String remoteCallReplyJsonStr = createURLAndGETConnect(url);

                //Read value into the object
                BankModel bankModel = new Gson().fromJson(remoteCallReplyJsonStr, BankModel.class);

                //add to list
                banks.add(bankModel);

            }//For Loop

            //send banks
            String resultAsString = new ObjectMapper().writeValueAsString(banks);
            return resultAsString;

        } catch (Exception e) {
            throw new RuntimeException("Error while processing request.");
        }
    }


    private static String createURLAndGETConnect(
            String urlStr) throws IOException {

        URL url = new URL(urlStr);

        HttpURLConnection connection = connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(HttpMethod.GET.toString());

        connection.connect();

        return IOUtils.toString(connection.getInputStream());

    }


}
