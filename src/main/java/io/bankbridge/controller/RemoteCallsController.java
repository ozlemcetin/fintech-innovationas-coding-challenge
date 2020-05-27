package io.bankbridge.controller;

import io.bankbridge.model.BankModel;
import io.bankbridge.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Route;


public class RemoteCallsController {

    //public as being used in test scenarios
    public static final String EXAMPLE_NAME = "Royal Bank of Boredom";
    public static final String EXAMPLE_BIC = "5678";
    public static final String EXAMPLE_AUTH = "SSL";

    //http://localhost:1234/rbb
    public static Route serveGetRBB = (Request request, Response response) -> {

        BankModel bankModel = new BankModel("1234", EXAMPLE_NAME, "GB", "OAUTH");
        return JsonUtil.objectToJson(bankModel);
    };

    //http://localhost:1234/cs
    public static Route serveGetCS = (Request request, Response response) -> {

        BankModel bankModel = new BankModel(EXAMPLE_BIC, "Credit Sweets", "CH", "OpenID");
        return JsonUtil.objectToJson(bankModel);
    };

    //http://localhost:1234/bes
    public static Route serveGetBES = (Request request, Response response) -> {

        BankModel bankModel = new BankModel("9870", "Banco de espiritu santo", "PT", EXAMPLE_AUTH);
        return JsonUtil.objectToJson(bankModel);
    };

}
