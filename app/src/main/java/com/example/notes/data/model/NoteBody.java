package com.example.notes.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.notes.domain.model.NoteModel;

import java.util.Date;
@Entity(tableName = "notes")
public class NoteBody implements NoteModel {

    public NoteBody(){}
    public NoteBody(NoteModel note){
        this.text = note.getText();
        this.lastEdit = note.getLastEditDate();
        this.uid = note.getUID();
    }
    @PrimaryKey(autoGenerate = true)
    public Integer uid;

    @ColumnInfo(name = "content_note")
    public String text;

    public Date lastEdit;
    @Override
    public Integer getUID() {
        return uid;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getLastEditDate() {
        return lastEdit;
    }
}
