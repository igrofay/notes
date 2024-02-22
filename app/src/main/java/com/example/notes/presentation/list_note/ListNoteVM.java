package com.example.notes.presentation.list_note;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.notes.domain.repos.NotesRepos;
import com.example.notes.presentation.add_or_edit_note.AddOrEditNoteSideEffect;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

@HiltViewModel
public class ListNoteVM extends ViewModel {
    private final NotesRepos notesRepos;
    private Disposable disposableGetListNoteFromRepos;
    private final BehaviorSubject<ListNoteState> stateSubject = BehaviorSubject
            .createDefault(new ListNoteState(new ArrayList<>()));
    private final PublishSubject<ListNoteSideEffect> sideEffectSubject = PublishSubject.create();
    public Observable<ListNoteState> observeState() {
        return stateSubject.hide();
    }
    public Observable<ListNoteSideEffect> observableSideEffect(){
        return sideEffectSubject.hide();
    }
    @Inject
    public ListNoteVM( NotesRepos notesRepos) {
        this.notesRepos = notesRepos;
        loadListNotes();
    }
    private void loadListNotes(){
        disposableGetListNoteFromRepos = notesRepos.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> stateSubject.onNext(new ListNoteState(list)),
                        throwable -> Log.e("ListNoteVM",throwable.getMessage().toString())
                );
    }
    void onEvent(ListNoteEvent event){
        if (event instanceof ListNoteEvent.OpenNote){
            sideEffectSubject.onNext(
                    new ListNoteSideEffect
                            .OpeningNote(((ListNoteEvent.OpenNote) event).uid)
            );
        }
    }
    @Override
    protected void onCleared() {
        disposableGetListNoteFromRepos.dispose();
        super.onCleared();
    }
}
