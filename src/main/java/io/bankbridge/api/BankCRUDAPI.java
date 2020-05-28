package io.bankbridge.api;

import io.bankbridge.controller.BankCRUDController;
import io.bankbridge.util.Filters;
import io.bankbridge.util.Paths;
import spark.Service;

public class BankCRUDAPI {

    //Public as being used in test scenarios
    public static final int DEFAULT_PORT = 4567;

    public static void main(String[] args) {

        //Multiple Spark servers in a single JVM
        Service http1 = Service.ignite().port(DEFAULT_PORT).threadPool(20);

        // Set up before-filters (called before each get/post)
        http1.before("*", Filters.addTrailingSlashes);

        http1.post(Paths.API_BANK_JSON, BankCRUDController.servePostBankJson);
        http1.post(Paths.API_BANK_QUERY_PARAMS, BankCRUDController.servePostBankQueryParams);
        http1.get(Paths.API_BANKS, BankCRUDController.serveGetBanks);
        http1.get(Paths.API_BANK_ID, BankCRUDController.serveGetBankId);
        http1.put(Paths.API_BANK_JSON_ID, BankCRUDController.servePutBankJsonId);
        http1.put(Paths.API_BANK_QUERY_PARAMS_ID, BankCRUDController.servePutBankQueryParamsId);
        http1.delete(Paths.API_BANK_ID_DELETE, BankCRUDController.serveDeleteBankId);
        http1.options(Paths.API_BANK_ID_PUT_OPTIONS, BankCRUDController.serveOptionsBankId);

        //Set up after-filters (called after each get/post)
        http1.after("*", Filters.addResponseTypeJson);

    }

}

