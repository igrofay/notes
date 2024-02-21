package com.example.notes.domain.model;

import java.util.Date;
import java.util.UUID;

public interface NoteItemModel {
    Integer getUID();
    String getTitle();
    String getText();
    Date getCreationDate();
}
