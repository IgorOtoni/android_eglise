package com.example.eu7340.egliseteste.utils;

public class ViaCEPException extends Exception {
    private String CEP;

    public ViaCEPException(String message) {
        super(message);

        this.CEP = "";
    }

    public ViaCEPException(String message, String cep) {
        super(message);

        this.CEP = cep;
    }

    public void setCEP(String cep) {
        this.CEP = cep;
    }

    public String getCEP() {
        return this.CEP;
    }

    public boolean hasCEP() {
        return !this.CEP.isEmpty();
    }
}
