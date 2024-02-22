package com.example.notes.presentation.list_note;

import com.example.notes.domain.model.NoteItemModel;

import java.util.Collections;
import java.util.List;

public record ListNoteState(List<NoteItemModel> notes) {
    public ListNoteState(List<NoteItemModel> notes) {
        this.notes = Collections.unmodifiableList(notes);
    }
}
