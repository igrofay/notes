package com.example.notes.presentation.list_note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notes.R;
import com.example.notes.databinding.FragmentListNoteBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListNoteFragment extends Fragment {

    private FragmentListNoteBinding binding;

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
    }
}