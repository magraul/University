package me.repositories;

import me.entities.Donator;

import java.util.List;

public interface DonatoriRepository extends Repository<Integer, Donator> {
    public List<Donator> findDonatoriDupaNume(String nume);
    public List<Donator> findDonatoriDupaAdresa(String adresa);
}
