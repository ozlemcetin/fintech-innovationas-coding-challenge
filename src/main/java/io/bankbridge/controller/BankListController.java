package io.bankbridge.controller;

import io.bankbridge.service.BanksService;
import io.bankbridge.service.BanksServiceCacheImp;
import io.bankbridge.service.BanksServiceRemoteCallsImp;
import io.bankbridge.util.JsonUtil;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;


public class BankListController {

    private static BanksService service;

    public BankListController(BanksService service) {
        this.service = service;
    }

    private String handle(Request request, Response response) {

        try {

            response.status(HttpStatus.OK_200);
            return service.getBanks();

        } catch (Exception e) {

            response.status(HttpStatus.NOT_FOUND_404);
            return JsonUtil.objectToJson(e.toString());
        }
    }


    public static Route serveGetV1BanksAll = (Request request, Response response) -> {

        return new BankListController(new BanksServiceCacheImp()).handle(request, response);
    };

    public static Route serveGetV2BanksAll = (Request request, Response response) -> {

        return new BankListController(new BanksServiceRemoteCallsImp()).handle(request, response);
    };

}
