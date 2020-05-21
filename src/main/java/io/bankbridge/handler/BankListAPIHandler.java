package io.bankbridge.handler;

import com.google.gson.Gson;
import io.bankbridge.response.BankBridgeResponse;
import io.bankbridge.response.StatusResponse;
import io.bankbridge.service.BanksService;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;


public class BankListAPIHandler {

    private static BanksService service;

    public BankListAPIHandler(BanksService service) {
        this.service = service;
    }

    public String handle(Request request, Response response) {

        response.type("application/json");

        try {

            response.status(HttpStatus.OK_200);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.SUCCESS,
                            service.getBanks()));

        } catch (Exception e) {

            response.status(HttpStatus.NOT_FOUND_404);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.ERROR, e.toString()));
        }
    }
}
