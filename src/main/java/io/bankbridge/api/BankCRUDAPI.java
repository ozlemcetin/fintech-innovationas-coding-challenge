package io.bankbridge.api;

import com.google.gson.Gson;
import io.bankbridge.exception.BankModelException;
import io.bankbridge.model.BankModel;
import io.bankbridge.response.BankBridgeResponse;
import io.bankbridge.response.StatusResponse;
import io.bankbridge.service.BankCRUDService;
import io.bankbridge.service.BankCRUDServiceMapImp;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.Collection;

public class BankCRUDAPI {

    //Public as being used in test scenarios
    public static final int DEFAULT_PORT = 4567;

    private static final BankCRUDService service = new BankCRUDServiceMapImp();

    public static void main(String[] args) {

        //Multiple Spark servers in a single JVM
        Service http1 = Service.ignite().port(DEFAULT_PORT).threadPool(20);

        //Below is the post method response handler which will add a BankModel
        /*
        POST http://localhost:4567/bank/json
        {
            "bic": "1012",
            "name": "Turkiye Is Bankasi",
            "countryCode": "TR",
            "auth": "OAUTH"
        }
         */
        http1.post("/bank/json", (request, response) -> {

            response.type("application/json");

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
        });

        // Creates a new bank  resource, will return the ID to the created resource
        // attributes are sent as query parameters e.g.
        // /bank/queryParams?bic=1012&name=Turkiye%20Is%20Bankasi&countryCode=TR&auth=SSL
        http1.post("/bank/queryParams", (request, response) -> {

            response.type("application/json");

            String bic = request.queryParams("bic");
            String name = request.queryParams("name");
            String countryCode = request.queryParams("countryCode");
            String auth = request.queryParams("auth");

            BankModel bankModel = new BankModel(bic, name, countryCode, auth);
            service.addBankModel(bankModel);

            response.status(HttpStatus.CREATED_201);
            return new Gson()
                    .toJson(new BankBridgeResponse(StatusResponse.SUCCESS, "Bank Added"));
        });

        //get method response handler which returns all bank models
        http1.get("/banks", (request, response) -> {

            response.type("application/json");
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
        });

        //the get method response handler which returns a bank model with the given id
        http1.get("/bank/:id", (request, response) -> {

            response.type("application/json");
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
        });

        // put method response handler,
        // which edits the bank having the id supplied in the route pattern
        http1.put("/bank/json/:id", (request, response) -> {

            response.type("application/json");

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
        });

        // put method response handler,
        // which edits the bank having the id supplied in the route pattern
        // attributes are sent as query parameters e.g.
        // /bank/queryParams/<id>?title=ING

        http1.put("/bank/queryParams/:id", (request, response) -> {

            response.type("application/json");

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

        });

        //the delete method response handler,
        // which will delete the bank with the given id
        http1.delete("/bank/:id", (request, response) -> {

            response.type("application/json");
            service.deleteBankModel(request.params(":id"));

            response.status(HttpStatus.OK_200);
            return new Gson().toJson(
                    new BankBridgeResponse(StatusResponse.SUCCESS, "Bank Deleted"));
        });

        /*
        The options method is a good choice for conditional checking.
        Below is the options method response handler which will check whether a User with the given id exists
         */

        http1.options("/bank/:id", (request, response) -> {

            response.type("application/json");
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
        });
    }

}

