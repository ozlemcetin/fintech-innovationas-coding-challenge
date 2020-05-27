package io.bankbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModel;
import io.bankbridge.util.HttpURLConnectionUtil;
import io.bankbridge.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BanksServiceRemoteCallsImp implements BanksService {

    private static final String FILE_NAME = "banks-v2.json";

    //Public as being used in test scenarios
    public static Map<String, String> config;

    public static void init() throws Exception {

        //get the config values from file
        config = new ObjectMapper().readValue(
                Thread.currentThread().getContextClassLoader().getResource(FILE_NAME), Map.class);

    }

    public String getBanks() {

        try {

            //initialize
            init();

            List<BankModel> banks = new ArrayList<>();
            for (String key : config.keySet()) {

                String url = config.get(key);
                String remoteCallReplyJsonStr = HttpURLConnectionUtil.createURLAndGETConnect(url);

                //Read value into the object
                BankModel bankModel = new ObjectMapper().readValue(remoteCallReplyJsonStr, BankModel.class);

                //add to list
                banks.add(bankModel);

            }//For Loop

            //send banks
            //String resultAsString = new ObjectMapper().writeValueAsString(banks);
            return JsonUtil.objectToJson(banks);

        } catch (Exception e) {
            throw new RuntimeException("Error while processing request.");
        }
    }


}
