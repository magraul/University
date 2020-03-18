package me.repositories;


import me.JdbcUtils;
import me.entities.Entity;

import java.io.FileNotFoundException;

public interface Repository <TIP_ID, TIP_ENTITATE extends Entity<TIP_ID>>{

    /**
     *
     * @param elem
     * elem must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * if the entity is not valid
     * @throws IllegalArgumentException
     * if the given entity is null. *
     */
    TIP_ENTITATE save(TIP_ENTITATE elem);


    /**
     *
     * @param elem
     * entity must not be null
     * @return null - if the entity is updated,
     * otherwise returns the entity - (e.g id does not
    exist).
     * @throws IllegalArgumentException
     * if the given entity is null.
     * if the entity is not valid.
     */
    TIP_ENTITATE update(TIP_ENTITATE elem);

    /**
     * removes the entity with the specified id
     * @param elem
     * id must be not null
     * @return the removed entity or null if there is no entity with the
    given id
     * @throws IllegalArgumentException
     * if the given id is null.
     */
    TIP_ENTITATE delete(TIP_ID elem) throws IllegalArgumentException;

    /**
     *
     * @param id -the id of the entity to be returned
     * id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     * if id is null.
     */
    TIP_ENTITATE get(TIP_ID id);


    int size();


    /**
     *
     * @return all entities
     */
    Iterable<TIP_ENTITATE> findAll();
}