package io.bankbridge.api;

import org.eclipse.jetty.http.HttpMethod;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpURLConnectionResponse {

    private String inputStream;
    private int responseCode;
    private Map<String, List<String>> map;

    public HttpURLConnectionResponse(String inputStream, int responseCode, Map<String, List<String>> map) {
        this.inputStream = inputStream;
        this.responseCode = responseCode;
        this.map = map;
    }

    //helper
    public static HttpURLConnectionResponse createURLAndConnect(
            int port, String path, HttpMethod requestMethod, String jsonInputString) throws IOException {

        URL url = new URL("http://localhost:" + port + path);

        HttpURLConnection connection = connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(requestMethod.toString());

        //if jsonInputString available for POST and PUT methods
        if (jsonInputString != null &&
                (HttpMethod.POST.equals(requestMethod)
                        || HttpMethod.PUT.equals(requestMethod))) {
            /*
            to send the request content in JSON form.
            This parameter has to be set to send the request body in JSON format.
            Failing to do so, the server returns HTTP status code “400-bad request”.
             */
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            /*
            Set the “Accept” request header to “application/json” to read the response in the desired format:
             */
            connection.setRequestProperty("Accept", "application/json");

            /*
            To send request content, let's enable the URLConnection object's doOutput property to true.
            Otherwise, we'll not be able to write content to the connection output stream:
             */
            connection.setDoOutput(true);

            /*
            Create the Request Body
            After creating a custom JSON String, We'd need to write it:
             */
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        connection.connect();

        String inputStream = IOUtils.toString(connection.getInputStream());
        int responseCode = connection.getResponseCode();
        Map<String, List<String>> map = connection.getHeaderFields();

        return new HttpURLConnectionResponse(inputStream,
                responseCode, map);

    }


    public String getInputStream() {
        return inputStream;
    }

    public void setInputStream(String inputStream) {
        this.inputStream = inputStream;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<String>> map) {
        this.map = map;
    }
}
