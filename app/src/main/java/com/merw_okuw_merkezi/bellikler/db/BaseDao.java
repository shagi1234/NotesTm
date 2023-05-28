package com.merw_okuw_merkezi.bellikler.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<E> {

    @Insert
    long insert(E entity);

    @Insert
    void insert(List<E> entity);

    @Update
    void update(E entity);

    @Update
    void update(E... entity);

    @Delete
    void delete(E entity);

    @Delete
    void delete(E... entity);


}
