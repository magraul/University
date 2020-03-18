package me.exceptions;

public class ValidationException extends RuntimeException {
    String eroare;

    public ValidationException(String eroare) {
        this.eroare = eroare;
    }

    public String getEroare() {
        return eroare;
    }
}
