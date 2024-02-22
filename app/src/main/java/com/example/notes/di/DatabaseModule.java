package com.example.notes.di;

import android.content.Context;

import androidx.room.Room;

import com.example.notes.data.database.AppDatabase;
import com.example.notes.data.database.NotesDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@ApplicationContext Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "database-notes").build();
    }
    @Provides
    @Singleton
    NotesDao provideNotesDao(AppDatabase db){
        return db.notesDao();
    }
}
