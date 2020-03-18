package me.entities;

public abstract class Entity<TIP_ID> {
    private TIP_ID id;
    public TIP_ID getId(){
        return id;
    }
    public void setId(TIP_ID id) {
        this.id = id;
    }


}
