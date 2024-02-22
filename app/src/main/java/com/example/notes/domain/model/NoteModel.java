package com.example.notes.domain.model;

import java.util.Date;

public interface NoteModel {
    Integer getUID();
    String getText();
    Date getLastEditDate();
}
