package com.merw_okuw_merkezi.bellikler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.merw_okuw_merkezi.bellikler.databinding.ItemNoteBinding;
import com.merw_okuw_merkezi.bellikler.fragments.FragmentAddNote;
import com.merw_okuw_merkezi.bellikler.model.Note;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.NotesHolder> {
    private final Context context;
    private List<Note> notes=new ArrayList<>();
    private final FragmentActivity activity;

    public AdapterNotes(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterNotes.NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemNoteBinding itemNoteBinding = ItemNoteBinding.inflate(layoutInflater);
        return new NotesHolder(itemNoteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotes.NotesHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        ItemNoteBinding b;

        public NotesHolder(@NonNull ItemNoteBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            Note note = notes.get(getAdapterPosition());
            //set components
            b.content.setText(note.getContent());
            b.title.setText(note.getTitle());

            b.getRoot().setOnClickListener(v -> {
                addFragment(activity.getSupportFragmentManager(), R.id.fragment_container_full, FragmentAddNote.newInstance(notes.get(getAdapterPosition()).getId()));
            });

        }

        public void addFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = fragmentManager;
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(containerViewId, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
