package com.example.notes.presentation.list_note;

public abstract class ListNoteEvent {
    static class OpenNote extends ListNoteEvent{
        final Integer uid;

        public OpenNote(Integer uid) {
            this.uid = uid;
        }
    }
}
