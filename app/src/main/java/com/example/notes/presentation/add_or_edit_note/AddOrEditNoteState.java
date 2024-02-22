package com.example.notes.presentation.add_or_edit_note;

import androidx.annotation.NonNull;

import com.example.notes.domain.model.NoteModel;

import org.jetbrains.annotations.Contract;

class AddOrEditNoteState {
    public WhereIsTextInputFrom getWhereIsTextInputFrom() {
        return whereIsTextInputFrom;
    }
    private Integer uid;
    enum ActionStatus{
        EDIT,
        ADD
    }
    enum WhereIsTextInputFrom{
        KEYBOARD,
        AUDIO,
        NONE
    }
    private final ActionStatus actionStatus;
    private final WhereIsTextInputFrom whereIsTextInputFrom;
    private final String text ;
    private final Boolean isVoiceInput;
    private AddOrEditNoteState(ActionStatus actionStatus, WhereIsTextInputFrom whereIsTextInputFrom) {
        this.actionStatus = actionStatus;
        this.text = "";
        this.whereIsTextInputFrom = whereIsTextInputFrom;
        this.isVoiceInput = false;
    }

    private AddOrEditNoteState(
            ActionStatus actionStatus,
            WhereIsTextInputFrom whereIsTextInputFrom,
            @NonNull NoteModel note
    ) {
        this.actionStatus = actionStatus;
        this.text = note.getText();
        this.whereIsTextInputFrom = whereIsTextInputFrom;
        this.isVoiceInput = false;
        this.uid = note.getUID();
    }
    private AddOrEditNoteState(
            ActionStatus actionStatus,
            WhereIsTextInputFrom whereIsTextInputFrom,
            @NonNull String text,
            Integer uid
    ) {
        this.actionStatus = actionStatus;
        this.text = text;
        this.whereIsTextInputFrom = whereIsTextInputFrom;
        this.isVoiceInput = false;
        this.uid = uid;
    }
    private AddOrEditNoteState(
            ActionStatus actionStatus,
            WhereIsTextInputFrom whereIsTextInputFrom,
            @NonNull String text,
            Boolean isVoiceInput,
            Integer uid
    ) {
        this.actionStatus = actionStatus;
        this.text = text;
        this.whereIsTextInputFrom = whereIsTextInputFrom;
        this.isVoiceInput = isVoiceInput;
        this.uid = uid;
    }

    @NonNull
    static AddOrEditNoteState initAddStatus() {
        return new AddOrEditNoteState(ActionStatus.ADD, WhereIsTextInputFrom.NONE);
    }

    @NonNull
    static AddOrEditNoteState initEditStatus(){
        return new AddOrEditNoteState(ActionStatus.EDIT, WhereIsTextInputFrom.NONE);
    }
    AddOrEditNoteState setNoteData(@NonNull NoteModel model){
        return new AddOrEditNoteState(this.actionStatus, WhereIsTextInputFrom.NONE, model);
    }
    @NonNull
    AddOrEditNoteState updateText(WhereIsTextInputFrom whereIsTextInputFrom, @NonNull String text){
        return new AddOrEditNoteState(this.actionStatus, whereIsTextInputFrom, text,this.isVoiceInput, this.uid);
    }

    AddOrEditNoteState startVoiceInput(){
        return new AddOrEditNoteState(this.actionStatus, this.whereIsTextInputFrom, this.text, true, this.uid);
    }
    AddOrEditNoteState stopVoiceInput(){
        return new AddOrEditNoteState(this.actionStatus, this.whereIsTextInputFrom, this.text, this.uid);
    }

    @NonNull
    public ActionStatus getActionStatus() {
        return actionStatus;
    }
    public Boolean getIsVoiceInput() {
        return isVoiceInput;
    }
    @NonNull
    public String getText(){
        return text;
    }
    public Integer getUid(){ return uid;}
}
