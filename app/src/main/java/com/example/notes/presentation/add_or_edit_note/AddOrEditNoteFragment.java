package com.example.notes.presentation.add_or_edit_note;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notes.R;
import com.example.notes.databinding.FragmentAddOrEditNoteBinding;

import java.util.ArrayList;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class AddOrEditNoteFragment extends Fragment {
    private AddOrEditNoteVM model;
    private final AddNoteMenuProvider addNoteMenuProvider = new AddNoteMenuProvider();
    private FragmentAddOrEditNoteBinding binding;
    private Boolean isVoiceInput = false;
    private Disposable subscriptionStateDisposable;
    private Disposable subscriptionSideEffctDisposable;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private AddOrEditNoteState.ActionStatus actionStatus;
    private Menu menu;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            model.onEvent(new AddOrEditNoteEvent.InputTextFromKeyboard(s.toString()));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSpeechRecognizer();
        getActivity().addMenuProvider(addNoteMenuProvider);
        model = new ViewModelProvider(this).get(AddOrEditNoteVM.class);
    }
    private void initSpeechRecognizer(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireContext().getPackageName());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
                model.onEvent(new AddOrEditNoteEvent.StopVoiceInput());
            }

            @Override
            public void onError(int error) {
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> listText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                model.onEvent(new AddOrEditNoteEvent.InputTextFromAudio(listText));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscriptionStateDisposable.dispose();
        subscriptionSideEffctDisposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().removeMenuProvider(addNoteMenuProvider);
        speechRecognizer.destroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOrEditNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.editTextNote.addTextChangedListener(textWatcher);
        binding.speckingButton.setOnClickListener(this::speckingButtonOnClickListener);
        subscriptionStateDisposable = model.observeState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::render,
                        throwable -> Log.e("AddOrEditNoteFragment::render", throwable.getMessage())
                );
        subscriptionSideEffctDisposable = model.observableSideEffect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSideEffect);
    }
    private void handleSideEffect(AddOrEditNoteSideEffect sideEffect){
        if (sideEffect instanceof AddOrEditNoteSideEffect.VoiceInputStarted){
            speechRecognizer.startListening(speechRecognizerIntent);
        } else if (sideEffect instanceof AddOrEditNoteSideEffect.VoiceInputCompleted) {
            speechRecognizer.stopListening();
        }else if (sideEffect instanceof AddOrEditNoteSideEffect.NoteDeleted){
            NavHostFragment.findNavController(this)
                    .popBackStack();
        }
    }
    private void render(AddOrEditNoteState state){
        if (state.getWhereIsTextInputFrom() != AddOrEditNoteState.WhereIsTextInputFrom.KEYBOARD)
            updateEditText(state.getText());
        isVoiceInput = state.getIsVoiceInput();
        if (isVoiceInput){
            binding.speckingButton
                    .setImageDrawable(
                            getDrawable(requireContext(), R.drawable.ic_record_voice)
                    );
        }else {
            binding.speckingButton
                    .setImageDrawable(
                            getDrawable(requireContext(), R.drawable.ic_mic)
                    );
        }
        if (actionStatus == null){
            actionStatus = state.getActionStatus();
            updateMenu();
        }
    }
    private void updateMenu(){
        if (menu != null){
            MenuItem item = menu.findItem(R.id.delete_note);
            if (item != null && actionStatus == AddOrEditNoteState.ActionStatus.EDIT) {
                item.setVisible(true);
            }
        }
    }
    private void updateEditText(String text){
        int selectionStart = binding.editTextNote.getSelectionStart();
        int selectionEnd = binding.editTextNote.getSelectionEnd();
        binding.editTextNote.setText(text);
        binding.editTextNote.setSelection(
                Math.min(selectionStart, text.length()),
                Math.min(selectionEnd, text.length())
        );
    }
    private void speckingButtonOnClickListener(View view){
        if (isVoiceInput){
            model.onEvent(new AddOrEditNoteEvent.StopVoiceInput());
        }else {
            model.onEvent(new AddOrEditNoteEvent.StartVoiceInput());
        }
    }
    private class AddNoteMenuProvider implements MenuProvider{

        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_add_note, menu);
            AddOrEditNoteFragment.this.menu = menu;
            updateMenu();
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.delete_note){
                model.onEvent(new AddOrEditNoteEvent.DeleteNote());
              return true;
            }
            return false;
        }

    }
}