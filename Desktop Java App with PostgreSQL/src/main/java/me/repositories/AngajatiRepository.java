package me.repositories;

import me.entities.Angajat;

import java.util.List;

public interface AngajatiRepository extends Repository<Integer, Angajat>{
    public List<Angajat> findAngajatiDupaNume(String nume);
}
