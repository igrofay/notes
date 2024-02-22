package com.example.notes.presentation.list_note;

public abstract class ListNoteSideEffect {
    static class OpeningNote extends ListNoteSideEffect{
        final Integer uid;
        OpeningNote(Integer uid) {
            this.uid = uid;
        }
    }
}
