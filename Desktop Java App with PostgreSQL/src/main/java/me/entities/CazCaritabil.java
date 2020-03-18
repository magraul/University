package me.entities;

public class CazCaritabil extends Entity<Integer> {
    private String description;

    public CazCaritabil(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
