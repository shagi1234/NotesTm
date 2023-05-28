package com.merw_okuw_merkezi.bellikler.db;

import androidx.room.Dao;
import androidx.room.Query;

import com.merw_okuw_merkezi.bellikler.model.Note;

import java.util.List;

@Dao
public interface DaoNote extends BaseDao<Note> {

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Query("UPDATE note SET title=:title , content =:content WHERE id=:id")
    void updateValues(String title, String content, int id);

    @Query("DELETE FROM note WHERE id=:id")// id boyuncha delete edyar
    void delete(int id);

    @Query("SELECT *" +
            " FROM note" +
            " WHERE id =:id")
    Note getNoteById(int id);

    @Query("SELECT *" +
            " FROM note" +
            " WHERE title " +
            "LIKE '%' || :search || '%' OR" +
            " content LIKE '%' || :search || '%'"
    )
    List<Note> searchNotes(String search);
}
