package me.entities;

public class CazCaritabilDTO {
    private String descriere;
    private Float sumaAdunata;

    public CazCaritabilDTO(String descriere, Float sumaAdunata) {
        this.descriere = descriere;
        this.sumaAdunata = sumaAdunata;
    }

    public String getDescriere() {
        return descriere;
    }

    public Float getSumaAdunata() {
        return sumaAdunata;
    }
}
