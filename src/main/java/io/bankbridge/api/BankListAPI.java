package io.bankbridge.api;

import io.bankbridge.controller.BankListController;
import io.bankbridge.controller.RemoteCallsController;
import io.bankbridge.util.Filters;
import io.bankbridge.util.Paths;
import spark.Service;

public class BankListAPI {

    //Public as being used in test scenarios
    public static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {

        //Multiple Spark servers in a single JVM
        Service http1 = Service.ignite().port(DEFAULT_PORT).threadPool(20);

        //http://localhost:8080/v1/banks/all
        http1.get(Paths.API_V1_BANKS_ALL, BankListController.serveGetV1BanksAll);
        http1.get(Paths.API_V2_BANKS_ALL, BankListController.serveGetV2BanksAll);

        //Set up after-filters (called after each get/post)
        http1.after("*", Filters.addResponseTypeJson);


        //Mock Remotes
        {
            Service http2 = Service.ignite().port(1234).threadPool(20);

            //http://localhost:1234/rbb
            http2.get(Paths.API_REMOTE_RBB, RemoteCallsController.serveGetRBB);
            http2.get(Paths.API_REMOTE_CS, RemoteCallsController.serveGetCS);
            http2.get(Paths.API_REMOTE_BES, RemoteCallsController.serveGetBES);

            //Set up after-filters (called after each get/post)
            http2.after("*", Filters.addResponseTypeJson);
        }
    }
}