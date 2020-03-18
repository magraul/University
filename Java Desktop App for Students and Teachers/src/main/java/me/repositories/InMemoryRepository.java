package me.repositories;

import javafx.collections.ObservableMap;
import me.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<TIP_ID, TIP_ENTITATE extends Entity<TIP_ID>> implements Repository<TIP_ID, TIP_ENTITATE> {

    protected Map<TIP_ID, TIP_ENTITATE> entities = new HashMap<>();

    @Override
    public TIP_ENTITATE save(TIP_ENTITATE entity) {
        if(entity == null) throw new IllegalArgumentException("entity must be not null");
        if(entities.containsKey(entity.getId())) return entity;
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public TIP_ENTITATE update(TIP_ENTITATE elem) {
        if(elem == null || !getMap().containsKey(elem.getId()))
            throw new IllegalArgumentException("entitate nula!");
        if(!entities.containsKey(elem.getId()))
            return null;


        entities.replace(elem.getId(), elem);
        return null;
    }

    @Override
    public TIP_ENTITATE delete(TIP_ID id) throws RuntimeException {
        if(id == null || !getMap().containsKey(id)) throw new IllegalArgumentException("id invalid");
        TIP_ENTITATE for_return = entities.get(id);
        entities.remove(id);
        return for_return;
    }



    @Override
    public TIP_ENTITATE get(TIP_ID id) {
        TIP_ENTITATE for_return = entities.get(id);
        if(for_return == null)
            throw new IllegalArgumentException("id invalid!");
        return for_return;
    }

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public Iterable<TIP_ENTITATE> findAll() {
        return entities.values();
    }

    public Map getMap() {
        return entities;
    }

    public List<TIP_ENTITATE> getAll(){
        return new ArrayList<>(entities.values());
    }


    protected void deleteAll() {
        entities.clear();
    }
}
