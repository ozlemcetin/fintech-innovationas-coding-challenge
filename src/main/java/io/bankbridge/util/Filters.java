package io.bankbridge.util;

import spark.Filter;
import spark.Request;
import spark.Response;

public class Filters {

    // If a user manually manipulates paths and forgets to add
    // a trailing slash, redirect the user to the correct path
    public static Filter addTrailingSlashes = (Request request, Response response) -> {

        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };


    // Enable Response Type for all responses
    public static Filter addResponseTypeJson = (Request request, Response response) -> {

        response.type("application/json");
    };
}
