package com.example.notes.presentation.add_or_edit_note;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.R;
import com.example.notes.databinding.FragmentAddOrEditNoteBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddOrEditNoteFragment extends Fragment {

    private final AddNoteMenuProvider addNoteMenuProvider = new AddNoteMenuProvider();
    private FragmentAddOrEditNoteBinding binding;
    private Menu menu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SpeechRecognizer.createSpeechRecognizer(getContext());
        getActivity().addMenuProvider(addNoteMenuProvider);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().removeMenuProvider(addNoteMenuProvider);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddOrEditNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private class AddNoteMenuProvider implements MenuProvider{

        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_add_note, menu);
            AddOrEditNoteFragment.this.menu = menu;
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.save_input){
              return true;
            }
            return false;
        }

    }
}