package me.entities;

import java.util.Objects;

public class Student extends Entity<Long> implements Comparable<Student>{
    private String nume;
    private String prenume;
    private String grupa;
    private String email;
    private String cadruDidacticIndrumatorLab;
    private float media;

    public Student(Long id, String nume, String prenume, String grupa, String email, String cadruDidacticIndrumatorLab, float media) {
        this.setId(id);
        this.nume = nume;
        this.media = media;
        this.prenume = prenume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Float.compare(student.media, media) == 0 &&
                Objects.equals(nume, student.nume) &&
                Objects.equals(prenume, student.prenume) &&
                Objects.equals(grupa, student.grupa) &&
                Objects.equals(email, student.email) &&
                Objects.equals(cadruDidacticIndrumatorLab, student.cadruDidacticIndrumatorLab);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, prenume, grupa, email, cadruDidacticIndrumatorLab, media);
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getGrupa() {
        return grupa;
    }

    public String getCadruDidacticIndrumatorLab() {
        return cadruDidacticIndrumatorLab;
    }

    @Override
    public int compareTo(Student o) {
        return (int)(this.media - o.media);
    }

    public String getEmail() {
        return email;
    }

    public float getMedia() {
        return media;
    }

    @Override
    public String toString() {
        return "Student: " + getNume() + " " + getPrenume();
    }

    @Override
    public String toString2() {
        return "studenti";
    }
}