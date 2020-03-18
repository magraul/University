package me.entities;

public class InregistrareDTO {
    private String student;
    private String userName;

    public InregistrareDTO(String student, String userName) {
        this.student = student;
        this.userName = userName;
    }


    public String getStudent() {
        return student;
    }

    public String getUserName() {
        return userName;
    }
}
