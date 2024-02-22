package com.example.notes.presentation.add_or_edit_note;

public abstract class AddOrEditNoteSideEffect {
    static class VoiceInputStarted extends AddOrEditNoteSideEffect {
    }
    static class VoiceInputCompleted extends AddOrEditNoteSideEffect {
    }
    static class NoteDeleted extends AddOrEditNoteSideEffect{

    }

}
