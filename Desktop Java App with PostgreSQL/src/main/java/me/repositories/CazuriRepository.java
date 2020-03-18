package me.repositories;

import me.entities.CazCaritabil;

import java.util.List;

public interface CazuriRepository extends Repository<Integer, CazCaritabil> {
    public List<CazCaritabil> findCazuriDupaDescriere(String descriere);
}
