package com.example.notes.presentation.add_or_edit_note;

import android.util.Log;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.domain.model.NoteModel;
import com.example.notes.domain.repos.NotesRepos;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

@HiltViewModel
public class AddOrEditNoteVM extends ViewModel {
    private String startedText = "";
    private final NotesRepos notesRepos;
    private final BehaviorSubject<AddOrEditNoteState> stateSubject;
    private final PublishSubject<AddOrEditNoteSideEffect> sideEffectSubject = PublishSubject.create();
    public Observable<AddOrEditNoteState> observeState() {
        return stateSubject.hide();
    }
    private Disposable disposableGetNoteFromRepos;
    public Observable<AddOrEditNoteSideEffect> observableSideEffect(){
        return sideEffectSubject.hide();
    }
    @Inject
    public AddOrEditNoteVM(SavedStateHandle handle, NotesRepos notesRepos) {
        this.notesRepos = notesRepos;
        Integer uid = handle.get("uid");
        if (uid == null){
            stateSubject = BehaviorSubject.createDefault(AddOrEditNoteState.initAddStatus());
        }else {
            stateSubject = BehaviorSubject.createDefault(AddOrEditNoteState.initEditStatus());
            loadNoteFromRepos(uid);
        }
    }
    public void onEvent(AddOrEditNoteEvent event){
        if (event instanceof AddOrEditNoteEvent.InputTextFromKeyboard){
            updateTextFromKeyboard(((AddOrEditNoteEvent.InputTextFromKeyboard) event).text);
        } else if (
                event instanceof AddOrEditNoteEvent.StartVoiceInput ||
                        event instanceof AddOrEditNoteEvent.StopVoiceInput
        ) {
            changeStatusVoidInput(event instanceof AddOrEditNoteEvent.StartVoiceInput);
        }else if (event instanceof AddOrEditNoteEvent.InputTextFromAudio){
            updateTextFromAudio(((AddOrEditNoteEvent.InputTextFromAudio) event).text);
        }else if (event instanceof AddOrEditNoteEvent.DeleteNote){
            deleteNote();
        }
    }
    private void deleteNote(){
        final AddOrEditNoteState state = stateSubject.getValue();
        if (state.getActionStatus() == AddOrEditNoteState.ActionStatus.ADD) return;
        notesRepos.deleteNote(state.getUid());
        sideEffectSubject.onNext(new AddOrEditNoteSideEffect.NoteDeleted());
    }
    private void changeStatusVoidInput(Boolean isStart){
        final AddOrEditNoteState state = stateSubject.getValue();
        if (isStart){
            sideEffectSubject.onNext(new AddOrEditNoteSideEffect.VoiceInputStarted());
            stateSubject.onNext(state.startVoiceInput());
        }else {
            sideEffectSubject.onNext(new AddOrEditNoteSideEffect.VoiceInputCompleted());
            stateSubject.onNext(state.stopVoiceInput());
        }
    }
    private void updateTextFromKeyboard(String text){
        final AddOrEditNoteState state = stateSubject.getValue();
        stateSubject.onNext(state.updateText(AddOrEditNoteState.WhereIsTextInputFrom.KEYBOARD, text));
    }
    private void updateTextFromAudio(ArrayList<String> text){
        String joinText = String.join("\n", text);
        if (joinText.isBlank()) return;
        final AddOrEditNoteState state = stateSubject.getValue();
        final String lastText = state.getText().isBlank() ? "" : state.getText() + "\n";
        stateSubject.onNext(
                state.updateText(
                        AddOrEditNoteState.WhereIsTextInputFrom.AUDIO,
                        lastText + joinText
                )
        );
    }
    private void loadNoteFromRepos(Integer uid){
        disposableGetNoteFromRepos = notesRepos.getNote(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        model -> {
                            startedText = model.getText();
                            stateSubject.onNext(
                                    stateSubject.getValue()
                                            .setNoteData(model));
                            },
                        throwable -> {
                            Log.e("AddOrEditNoteVM", "GetNoteFromRepos: "+ throwable.getMessage());
                        }
                );

    }
    private void saveNote(){
        AddOrEditNoteState state = stateSubject.getValue();
        if (startedText.equals(state.getText()) || state.getText().isBlank()) return;
        String text = state.getText().trim();
        switch (state.getActionStatus()){
            case ADD -> notesRepos.addNote(text);
            case EDIT -> notesRepos.editNote(new NoteModel() {
                @Override
                public Integer getUID() {
                    return state.getUid();
                }

                @Override
                public String getText() {
                    return text;
                }

                @Override
                public Date getLastEditDate() {
                    return new Date();
                }
            });
        }
    }
    @Override
    protected void onCleared() {
        if (disposableGetNoteFromRepos != null) disposableGetNoteFromRepos.dispose();
        saveNote();
        super.onCleared();
    }
}
