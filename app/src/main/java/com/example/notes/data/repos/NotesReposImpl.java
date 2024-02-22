package com.example.notes.data.repos;

import android.util.Log;

import com.example.notes.data.database.NotesDao;
import com.example.notes.data.model.NoteBody;
import com.example.notes.data.model.NoteItemBody;
import com.example.notes.domain.model.NoteItemModel;
import com.example.notes.domain.model.NoteModel;
import com.example.notes.domain.repos.NotesRepos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class NotesReposImpl implements NotesRepos {
    private final NotesDao notesDao;

    public NotesReposImpl(NotesDao notesDao) {
        this.notesDao = notesDao;
    }

    @Override
    public Flowable<List<NoteItemModel>> getNotes() {
        return notesDao.getAll().map(noteBodies -> {
            List<NoteItemModel> noteItemModels = new ArrayList<>();
            for (NoteModel noteModel: noteBodies){
                String[] text = splitText(noteModel.getText());
                noteItemModels.add(
                        new NoteItemBody(
                                noteModel.getUID(),
                                noteModel.getLastEditDate(),
                                text[0],
                                text[1]
                        )
                );
            }
            return noteItemModels;
        });
    }

    private String[] splitText(String text){
        String[] answer = new String[2];
        if (text.length() > 54){
            String substringStart = text.substring(0, 54);
            if (substringStart.contains("\n")){
                return text.split("\n", 2);
            }else {
                int spaceIndex = substringStart.lastIndexOf(' ');
                if (spaceIndex != -1){
                    answer[0]= text.substring(0,spaceIndex);
                    answer[1]= text.substring(spaceIndex-1);
                }else {
                    answer[0] =substringStart;
                    answer[1]=text.substring(54);
                }
            }
        }else {
            if (text.contains("\n")){
                return text.split("\n", 2);
            }else {
                answer[0] = text;
            }

        }
        return answer;
    }

    @Override
    public Single<NoteModel> getNote(Integer uid) {
        return notesDao.findByUid(uid).cast(NoteModel.class);
    }

    @Override
    public void addNote(String text) {
        NoteBody noteBody = new NoteBody(new NoteModel() {
            @Override
            public Integer getUID() {
                return null;
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public Date getLastEditDate() {
                return new Date();
            }
        });
        Schedulers.io().scheduleDirect(()-> notesDao.insert(noteBody));
    }

    @Override
    public void deleteNote(Integer uid) {
        Schedulers.io().scheduleDirect(()-> notesDao.delete(uid));
    }

    @Override
    public void editNote(NoteModel note) {
        NoteBody noteBody = new NoteBody(note);
        Schedulers.io().scheduleDirect(()-> notesDao.update(noteBody));
    }
}
