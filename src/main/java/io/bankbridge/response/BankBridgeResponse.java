package io.bankbridge.response;

import com.google.gson.JsonElement;

public class BankBridgeResponse {

    /*
    {
    status: <STATUS>
    message: <TEXT-MESSAGE>
    data: <JSON-OBJECT>
    }
    */

    private StatusResponse status;
    private String message;
    private JsonElement data;

    public BankBridgeResponse() {
    }

    public BankBridgeResponse(StatusResponse status) {
        this.status = status;
    }

    public BankBridgeResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public BankBridgeResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;
    }

    public BankBridgeResponse(StatusResponse status, String message, JsonElement data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
