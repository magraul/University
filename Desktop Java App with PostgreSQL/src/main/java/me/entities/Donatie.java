package me.entities;

public class Donatie extends Entity<Integer>{
    Float sumaDonata;
    Integer Iddonator;
    Integer IdcazCaritabil;

    public Donatie(Float sumaDonata, Integer donator, Integer cazCaritabil) {
        this.sumaDonata = sumaDonata;
        this.Iddonator = donator;
        this.IdcazCaritabil = cazCaritabil;
    }

    public Float getSumaDonata() {
        return sumaDonata;
    }

    public Integer getDonator() {
        return Iddonator;
    }

    public Integer getCazCaritabil() {
        return IdcazCaritabil;
    }
}
