package io.bankbridge.api;

import static spark.Spark.get;

public class HelloWorldAPI {

    public static void main(String[] args) {

        /*
        Run and view: http://localhost:4567/hello
        */

        get("/hello", (req, res) -> "Hello, World!");

        /*
        The main building block of a Spark application is a set of routes.
        A route is made up of three simple pieces:

        A verb (get, post, put, delete, head, trace, connect, options)
        A path (/hello, /users/:name)
        A callback (request, response) -> { }

        Routes are matched in the order they are defined. The first route that matches the request is invoked.
        Always statically import Spark methods to ensure good readability:
         */
        get("/hello/:name", (req, res) -> {
            return "Hello, " + req.params(":name") + "!";
        });

        /*
        By calling the stop() method the server is stopped and all routes are cleared.
        */
        //stop();

    }
}
