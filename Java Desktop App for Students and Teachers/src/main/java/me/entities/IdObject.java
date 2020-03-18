package me.entities;


import java.util.Objects;

public class IdObject {
    private String id1;
    private String id2;

    public IdObject(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdObject idObject = (IdObject) o;
        return id1.equals(idObject.id1) &&
                id2.equals(idObject.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }

    public String getId1() {
        return id1;
    }

    public String getId2() {
        return id2;
    }
}