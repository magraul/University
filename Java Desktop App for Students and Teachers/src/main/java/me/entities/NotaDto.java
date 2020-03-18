package me.entities;

public class NotaDto {
    private String student, tema, nota, data, profesor, feedbackk;
    private IdObject id;

    public NotaDto(IdObject id, String student, String tema, String nota, String feedbackk) {
        this.student = student;
        this.tema = tema;
        this.nota = nota;
        this.id = id;
        this.feedbackk = feedbackk;
    }

    public IdObject getId() {
        return id;
    }

    public NotaDto(String student, String tema, String nota, String data, String profesor, String feedbackk) {
        this.student = student;
        this.tema = tema;
        this.nota = nota;
        this.data = data;
        this.profesor = profesor;
        this.feedbackk = feedbackk;
    }

    public String getData() {
        return data;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getStudent() {
        return student;
    }

    public String getTema() {
        return tema;
    }

    public String getNota() {
        return nota;
    }

    public String getFeedbackk() {
        return feedbackk;
    }

    @Override
    public String toString() {
        return "NotaDto{" +
                "student='" + student + '\'' +
                ", tema='" + tema + '\'' +
                ", nota='" + nota + '\'' +
                '}';
    }
}
