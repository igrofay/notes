package com.example.notes.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.data.model.NoteBody;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY lastEdit DESC")
    Flowable<List<NoteBody>> getAll();

    @Query("SELECT * FROM notes WHERE uid = :uid")
    Single<NoteBody> findByUid(int uid);
    @Insert
    void insert(NoteBody noteBody);
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(NoteBody noteBody);
    @Query("DELETE FROM notes WHERE uid = :uid")
    void delete(int uid);
}
