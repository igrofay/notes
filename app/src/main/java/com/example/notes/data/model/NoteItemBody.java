package com.example.notes.data.model;

import com.example.notes.domain.model.NoteItemModel;

import java.util.Date;
import java.util.Objects;

public class NoteItemBody implements NoteItemModel {
    public NoteItemBody(){}

    public NoteItemBody(Integer uid, Date lastEdit, String title, String text) {
        this.uid = uid;
        this.lastEdit = lastEdit;
        this.title = title;
        this.text = text;
    }

    public Integer uid;
    public Date lastEdit;
    public String title;
    public String text;
    @Override
    public Integer getUID() {
        return uid;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getLastEditDate() {
        return lastEdit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteItemBody that = (NoteItemBody) o;

        if (!Objects.equals(uid, that.uid)) return false;
        if (!Objects.equals(lastEdit, that.lastEdit))
            return false;
        if (!Objects.equals(title, that.title)) return false;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (lastEdit != null ? lastEdit.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
