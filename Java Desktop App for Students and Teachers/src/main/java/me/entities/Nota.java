package me.entities;

import java.time.LocalDate;

public class Nota extends Entity<IdObject> {
    private LocalDate data;
    private String profesor;
    private Student student;
    private Tema tema;
    Integer valoare;
    boolean inregistrataCuIntarziereaProfului = false;
    String feedback;

    public Nota(LocalDate data, String profesor, Student student, Tema tema, Integer valoare, String feedback) {
        setId(new IdObject(student.getId().toString(), tema.getId().toString()));
        this.data = data;
        this.profesor = profesor;
        this.student = student;
        this.tema = tema;
        this.valoare = valoare;
        this.feedback = feedback;
    }

    public void setInregistrataCuIntarziereaProfului(boolean inregistrataCuIntarziereaProfului) {
        this.inregistrataCuIntarziereaProfului = inregistrataCuIntarziereaProfului;
    }

    public boolean isInregistrataCuIntarziereaProfului() {
        return inregistrataCuIntarziereaProfului;
    }

    public Integer getValoare() {
        return valoare;
    }

    public LocalDate getData() {
        return data;
    }

    public String getProfesor() {
        return profesor;
    }

    public Student getStudent() {
        return student;
    }

    public Tema getTema() {
        return tema;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "student=" + student +
                ", tema=" + tema +
                ", valoare=" + valoare +
                '}';
    }

    @Override
    public String toString2() {
        return null;
    }

    public String getFeedback() {
        return feedback;
    }
}
