package me.events;

import me.entities.Entity;
import me.entities.IdObject;
import me.entities.Nota;

public class EvenimentSchimbare implements Event {
    private TipEveniment tip;
    private Entity<Long> data, oldData;
    private Nota notaObj;

    public EvenimentSchimbare(TipEveniment tip, Entity<Long> data) {
        this.tip = tip;
        this.data = data;
    }

    public EvenimentSchimbare(TipEveniment tip, Entity<Long> data, Entity<Long> oldData) {
        this.tip = tip;
        this.data = data;
        this.oldData = oldData;
    }

    public EvenimentSchimbare(TipEveniment add, Nota nota) {
        this.tip = add;
        this.notaObj = nota;
    }

    public TipEveniment getTip() {
        return tip;
    }

    public Entity<Long> getData() {
        return data;
    }

    public Nota getNotaObj() {
        return notaObj;
    }

    public Entity<Long> getOldData() {
        return oldData;
    }
}
