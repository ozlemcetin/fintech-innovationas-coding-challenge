package io.bankbridge.api;

import com.google.gson.Gson;
import io.bankbridge.handler.BankListAPIHandler;
import io.bankbridge.model.BankModel;
import io.bankbridge.service.BanksServiceCacheImp;
import io.bankbridge.service.BanksServiceRemoteCallsImp;
import spark.Service;

public class BankListAPI {

    //Public as being used in test scenarios
    public static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {

        //Multiple Spark servers in a single JVM
        Service http1 = Service.ignite().port(DEFAULT_PORT).threadPool(20);


        //Rest v1
        {
            //http://localhost:8080/v1/banks/all
            http1.get("/v1/banks/all", (request, response) ->
                    new BankListAPIHandler(new BanksServiceCacheImp()).handle(request, response));
        }


        //Rest v2
        {
            //http://localhost:8080/v2/banks/all
            http1.get("/v2/banks/all", (request, response) ->
                    new BankListAPIHandler(new BanksServiceRemoteCallsImp()).handle(request, response));
        }


        //Mock Remotes
        {

            //Multiple Spark servers in a single JVM
            Service http2 = Service.ignite().port(1234).threadPool(20);

            //http://localhost:1234/rbb
            http2.get("/rbb", (request, response) -> {

                String jsonInputString = new Gson()
                        .toJson(new BankModel("1234", "Royal Bank of Boredom", "GB", "OAUTH"));
                return jsonInputString;
            });


            //http://localhost:1234/cs
            http2.get("/cs", (request, response) -> {

                String jsonInputString = new Gson()
                        .toJson(new BankModel("5678", "Credit Sweets", "CH", "OpenID"));
                return jsonInputString;
            });


            //http://localhost:1234/bes
            http2.get("/bes", (request, response) -> {

                String jsonInputString = new Gson()
                        .toJson(new BankModel("9870", "Banco de espiritu santo", "PT", "SSL"));
                return jsonInputString;
            });

        }

    }
}