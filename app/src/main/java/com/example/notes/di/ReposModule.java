package com.example.notes.di;


import com.example.notes.data.database.NotesDao;
import com.example.notes.data.repos.NotesReposImpl;
import com.example.notes.domain.repos.NotesRepos;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ReposModule {
    @Provides
    NotesRepos provideNotesRepos(NotesDao dao){
        return new NotesReposImpl(dao);
    }
}
