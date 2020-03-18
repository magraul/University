package me;

public class Student {
    private Integer id;
    private String nume;
    private String prenume;
    private Double media;

    public Student(Integer id, String nume, String prenume, Double media) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.media = media;
    }

    public Integer getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public Double getMedia() {
        return media;
    }

    public String getPrenume() {
        return prenume;
    }
}
