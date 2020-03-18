package me.entities;

public class Angajat extends Person{
    private String username;
    private String password;


    public Angajat(String name, String phoneNumber, String address, String username, String password) {
        super(name, phoneNumber, address);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
