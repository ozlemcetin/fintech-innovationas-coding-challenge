package io.bankbridge.model;

public class BankModel {

    private String bic;
    private String name;
    private String countryCode;
    private String auth;

    public BankModel() {
    }

    public BankModel(String bic, String name, String countryCode, String auth) {
        this.bic = bic;
        this.name = name;
        this.countryCode = countryCode;
        this.auth = auth;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "BankModel{" +
                "bic='" + bic + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", auth='" + auth + '\'' +
                '}';
    }
}
