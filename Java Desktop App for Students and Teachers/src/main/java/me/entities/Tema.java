package me.entities;

import java.util.Objects;

public class Tema extends Entity<Long> implements Comparable<Tema> {

    private String descriere;
    private Integer startWeek;
    private Integer deadlineWeek;
    private String materie;


    public Tema(Long id, String descriere, Integer startWeek, Integer deadlineWeek) {
        super.setId(id);
        this.descriere = descriere;
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
    }

    public Tema(Long id, String descriere, Integer startWeek, Integer deadlineWeek, String materie) {
        super.setId(id);
        this.descriere = descriere;
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
        this.materie = materie;
    }

    public String getMaterie() {
        return materie;
    }

    public Tema setDeadlineWeek(Integer deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
        return this;
    }

    public void setMaterie(String materie) {
        this.materie = materie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tema tema = (Tema) o;
        return Objects.equals(descriere, tema.descriere) &&
                Objects.equals(startWeek, tema.startWeek) &&
                Objects.equals(deadlineWeek, tema.deadlineWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriere, startWeek, deadlineWeek);
    }

    public String getDescriere() {
        return descriere;
    }

    public Integer getStartWeek() {
        return startWeek;
    }

    public Integer getDeadlineWeek() {
        return deadlineWeek;
    }


    @Override
    public int compareTo(Tema o) {
        return 0;
    }

    @Override
    public String toString() {
        return "tema: " + getDescriere() + " startWeek: " + getStartWeek() + " deadlineWeek: " + getDeadlineWeek() ;
    }

    @Override
    public String toString2() {
        return "teme";
    }
}