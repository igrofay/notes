package com.example.notes.presentation.list_note;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.databinding.FragmentListNoteBinding;
import com.example.notes.presentation.add_or_edit_note.AddOrEditNoteVM;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class ListNoteFragment extends Fragment {
    private ListNoteVM model;
    private FragmentListNoteBinding binding;
    private Disposable subscriptionStateDisposable;
    private Disposable subscriptionSideEffctDisposable;
    private final ListNoteAdapter listNoteAdapter = new ListNoteAdapter(uid->{
        model.onEvent(new ListNoteEvent.OpenNote(uid));
    });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(ListNoteVM.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addNewNoteButton.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_list_note_placeholder_to_add_note_placeholder)
        );
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.recyclerNotes.addItemDecoration(dividerItemDecoration);
        binding.recyclerNotes.setAdapter(listNoteAdapter);
        subscriptionStateDisposable = model.observeState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::render,
                        throwable -> Log.e("ListNoteFragment",throwable.getMessage().toString())
                );
        subscriptionSideEffctDisposable = model.observableSideEffect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSideEffect);
    }
    private void render(ListNoteState state){
        listNoteAdapter.updateList(state.notes());
    }

    private void handleSideEffect(ListNoteSideEffect sideEffect){
        if (sideEffect instanceof ListNoteSideEffect.OpeningNote){
            Bundle bundle = new Bundle();
            bundle.putInt("uid", ((ListNoteSideEffect.OpeningNote) sideEffect).uid);
            NavHostFragment.findNavController(this)
                    .navigate(
                            R.id.action_list_note_placeholder_to_edit_note_placeholder,
                            bundle
                    );
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscriptionStateDisposable.dispose();
        subscriptionSideEffctDisposable.dispose();
    }
}