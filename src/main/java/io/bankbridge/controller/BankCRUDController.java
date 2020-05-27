package io.bankbridge.controller;

import com.google.gson.Gson;
import io.bankbridge.exception.BankModelException;
import io.bankbridge.model.BankModel;
import io.bankbridge.response.BankBridgeResponse;
import io.bankbridge.response.StatusResponse;
import io.bankbridge.service.BankCRUDService;
import io.bankbridge.service.BankCRUDServiceMapImp;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;

public class BankCRUDController {

    private static final BankCRUDService service = new BankCRUDServiceMapImp();

    /*
    POST http://localhost:4567/bank/json
    {
        "bic": "1012",
        "name": "Turkiye Is Bankasi",
        "countryCode": "TR",
        "auth": "OAUTH"
    }
     */

    //Below is the post method response handler which will add a BankModel
    public static Route servePostBankJson = (Request request, Response response) -> {


        // the JSON representation of the BankModel object
        // is passed as the raw body of a POST request.
        //If the response is in JSON format,
        // use any third-party JSON parsers such as J
        // Jackson library, Gson, or org.json to parse the response.
        BankModel bankModel = new Gson().fromJson(request.body(), BankModel.class);
        service.addBankModel(bankModel);

        /*
            {
                "status":"SUCCESS"
            }
        */
        response.status(HttpStatus.CREATED_201);
        return new Gson()
                .toJson(new BankBridgeResponse(StatusResponse.SUCCESS, "Bank Added"));
    };


    // Creates a new bank  resource, will return the ID to the created resource
    // attributes are sent as query parameters e.g.
    // /bank/queryParams/?bic=1012&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL

    public static Route servePostBankQueryParams = (Request request, Response response) -> {

        String bic = request.queryParams("bic");
        String name = request.queryParams("name");
        String countryCode = request.queryParams("countryCode");
        String auth = request.queryParams("auth");

        BankModel bankModel = new BankModel(bic, name, countryCode, auth);
        service.addBankModel(bankModel);

        response.status(HttpStatus.CREATED_201);
        return new Gson()
                .toJson(new BankBridgeResponse(StatusResponse.SUCCESS, "Bank Added"));
    };

    //get method response handler which returns all bank models
    public static Route serveGetBanks = (Request request, Response response) -> {

        Collection<BankModel> bankModelCollection = service.getBankModels();

        /*
          {"status":"SUCCESS",
            "data":[{"bic":"1932","name":"ING","countryCode":"TR","auth":"SSL"},
            {"bic":"1012","name":"Turkiye Is Bankasi","countryCode":"TR","auth":"SSL"}]}
         */
        response.status(HttpStatus.OK_200);
        return new Gson()
                .toJson(new BankBridgeResponse(StatusResponse.SUCCESS,
                        "Banks Fetched",
                        new Gson()
                                .toJsonTree(bankModelCollection)));
    };

    //the get method response handler which returns a bank model with the given id
    public static Route serveGetBankId = (Request request, Response response) -> {

        BankModel bankModel = service.getBankModel(request.params(":id"));

        //Not Found
        if (bankModel == null) {
            response.status(HttpStatus.NOT_FOUND_404);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.ERROR,
                            "Bank Not Found"));
        }

        response.status(HttpStatus.OK_200);
        return new Gson()
                .toJson(new BankBridgeResponse(StatusResponse.SUCCESS,
                        "Bank Found",
                        new Gson()
                                .toJsonTree(bankModel)));
    };

    // put method response handler,
    // which edits the bank having the id supplied in the route pattern
    public static Route servePutBankJsonId = (Request request, Response response) -> {

        /*
           the data are passed in the raw body of a POST request as a JSON object
           whose property names match the fields of the Bank object to be edited.
         */
        try {

            BankModel bankModel = new Gson().fromJson(request.body(), BankModel.class);
            service.editBankModel(bankModel);

            response.status(HttpStatus.OK_200);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.SUCCESS,
                            "Bank Edited",
                            new Gson()
                                    .toJsonTree(bankModel)));

        } catch (BankModelException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.ERROR,
                            "Error in Edit!"));
        }
    };

    // put method response handler,
    // which edits the bank having the id supplied in the route pattern
    // attributes are sent as query parameters e.g.
    // /bank/queryParams/<id>?title=ING

    public static Route servePutBankQueryParamsId = (Request request, Response response) -> {

        String bic = request.params(":id");
        BankModel bankModel = service.getBankModel(bic);

        //Not Found
        if (bankModel == null) {
            response.status(HttpStatus.NOT_FOUND_404);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.ERROR,
                            "Bank Not Found"));
        }


        String name = request.queryParams("name");
        bankModel.setName(name);

        String countryCode = request.queryParams("countryCode");
        bankModel.setCountryCode(countryCode);

        String auth = request.queryParams("auth");
        bankModel.setAuth(auth);

        service.editBankModel(bankModel);

        response.status(HttpStatus.OK_200);
        return new Gson()
                .toJson(new BankBridgeResponse(StatusResponse.SUCCESS,
                        "Bank Edited",
                        new Gson()
                                .toJsonTree(bankModel)));

    };

    //the delete method response handler,
    // which will delete the bank with the given id

    public static Route serveDeleteBankId = (Request request, Response response) -> {

        service.deleteBankModel(request.params(":id"));

        response.status(HttpStatus.OK_200);
        return new Gson().toJson(
                new BankBridgeResponse(StatusResponse.SUCCESS, "Bank Deleted"));
    };

    /*
      The options method is a good choice for conditional checking.
      Below is the options method response handler which will check whether a User with the given id exists
     */

    public static Route serveOptionsBankId = (Request request, Response response) -> {

        boolean exists = service.bankModelExist(request.params(":id"));

        //Not Found
        if (!exists) {
            response.status(HttpStatus.NOT_FOUND_404);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.ERROR,
                            "Bank Not Found"));
        }


        response.status(HttpStatus.OK_200);
        return new Gson().toJson(
                new BankBridgeResponse(StatusResponse.SUCCESS,
                        "Bank Exists"));
    };

}
