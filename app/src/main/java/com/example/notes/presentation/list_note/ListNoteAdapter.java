package com.example.notes.presentation.list_note;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.domain.model.NoteItemModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ViewHolder> {
    private List<NoteItemModel> notes = new ArrayList<>();

    private final CallbackClickItem callbackClickItem;

    public ListNoteAdapter(CallbackClickItem callback) {
        callbackClickItem = callback;
    }

    public void updateList(List<NoteItemModel> notes){
        if (this.notes.equals(notes)) return;
        this.notes = notes;
        synchronized (this){
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_note_item, parent, false);
        return new ViewHolder(view, callbackClickItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateDate(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView content;
        private final TextView lastEdit;
        private Resources res;
        private Integer uidNote;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        public ViewHolder(@NonNull View itemView, CallbackClickItem callbackClickItem) {
            super(itemView);
            res = itemView.getResources();
            title = itemView.findViewById(R.id.titleNote);
            content = itemView.findViewById(R.id.contentNote);
            lastEdit = itemView.findViewById(R.id.lastEditDateNote);
            itemView.setOnClickListener(v->{
                callbackClickItem.onClick(uidNote);
            });
        }
        void updateDate(NoteItemModel model){
            uidNote = model.getUID();
            title.setText(model.getTitle());
            String contentText = model.getText() == null || model.getText().isBlank()
                    ?  res.getString(R.string.no_additional_text)
                    : model.getText();
            content.setText(contentText);
            String lastEditText = res.getText(R.string.last_edit) + ": " + sdf.format(model.getLastEditDate());
            lastEdit.setText(lastEditText);
        }
    }
    interface CallbackClickItem {
        void onClick(Integer uid);
    }
}
