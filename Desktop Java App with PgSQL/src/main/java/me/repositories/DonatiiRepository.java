package me.repositories;

import me.entities.CazCaritabil;
import me.entities.Donatie;
import me.entities.Donator;

import java.util.List;

public interface DonatiiRepository extends Repository<Integer, Donatie> {
    public List<Donatie> findDonatiiDupaSuma(Float suma);
    public List<Donatie> findDonatiiDupaCaz(CazCaritabil cazCaritabil);
    public List<Donatie> findDonatiiDupaDonator(Donator donator);
}
