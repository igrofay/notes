package com.example.notes.domain.repos;

import com.example.notes.domain.model.NoteItemModel;
import com.example.notes.domain.model.NoteModel;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface NotesRepos {
    Flowable<List<NoteItemModel>> getNotes();

    Single<NoteModel> getNote(Integer uid);

    void addNote(String text);

    void deleteNote(Integer uid);

    void editNote(NoteModel note);
}
