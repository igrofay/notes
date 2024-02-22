package com.example.notes.presentation.add_or_edit_note;

import java.util.ArrayList;

public abstract class AddOrEditNoteEvent {
    static class InputTextFromKeyboard extends AddOrEditNoteEvent {
        final String text;
        InputTextFromKeyboard(String text) {
            this.text = text;
        }
    }
    static class InputTextFromAudio extends AddOrEditNoteEvent{
        final ArrayList<String> text;
        InputTextFromAudio(ArrayList<String> text){ this.text = text; }
    }
    static class StartVoiceInput extends AddOrEditNoteEvent{
    }
    static class StopVoiceInput extends AddOrEditNoteEvent{
    }
    static class DeleteNote extends AddOrEditNoteEvent{
    }
}
