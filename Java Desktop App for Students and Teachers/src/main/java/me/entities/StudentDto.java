package me.entities;

public class StudentDto {
    private String id;
    private String numeComplet;
    private String grupa;

    public StudentDto(String id, String numeComplet, String grupa) {
        this.numeComplet = numeComplet;
        this.grupa = grupa;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getNumeComplet() {
        return numeComplet;
    }

    public String getGrupa() {
        return grupa;
    }

    @Override
    public String toString() {
        return "StudentDto{" +
                "numeComplet='" + numeComplet + '\'' +
                ", grupa='" + grupa + '\'' +
                '}';
    }
}


