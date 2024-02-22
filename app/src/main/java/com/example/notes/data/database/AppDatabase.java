package com.example.notes.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.notes.data.model.NoteBody;
import com.example.notes.data.utils.ConverterDate;

@Database(entities = {NoteBody.class}, version = 1)
@TypeConverters({ConverterDate.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotesDao notesDao();
}
