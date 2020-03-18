package me.events;

public class EvenimentSchimbare implements Event {
    private TipEveniment tip;

    public EvenimentSchimbare(TipEveniment donatie) {
        this.tip = donatie;
    }

    public TipEveniment getTip() {
        return this.tip;
    }
}
