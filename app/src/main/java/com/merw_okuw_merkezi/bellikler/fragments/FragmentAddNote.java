package com.merw_okuw_merkezi.bellikler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.merw_okuw_merkezi.bellikler.databinding.FragmentAddNoteBinding;
import com.merw_okuw_merkezi.bellikler.db.BellilerDatabase;
import com.merw_okuw_merkezi.bellikler.db.DaoNote;
import com.merw_okuw_merkezi.bellikler.model.Note;

public class FragmentAddNote extends Fragment {
    private FragmentAddNoteBinding b;
    private DaoNote daoNote;
    private int noteId;

    public static FragmentAddNote newInstance(int id) {
        FragmentAddNote fragment = new FragmentAddNote();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoNote = BellilerDatabase.getBellilerDatabase(getContext()).daoNote();
        if (getArguments() != null) {
            noteId = getArguments().getInt("id");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAddNoteBinding.inflate(inflater, container, false);
        initListeners();
        getNoteById();
        return b.getRoot();
    }

    private void getNoteById() {
        if (noteId == -1) {
            b.icBack.setVisibility(View.GONE);
            b.btnDelete.setVisibility(View.GONE);
            return;
        }
        b.btnDelete.setVisibility(View.VISIBLE);
        b.icBack.setVisibility(View.VISIBLE);

        BellilerDatabase.databaseWriteExecutor.execute(() -> {
            Note note = daoNote.getNoteById(noteId);

            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                b.edtContent.setText(note.getContent());
                b.edtTitle.setText(note.getTitle());
            });
        });
    }

    private void initListeners() {
        b.icBack.setOnClickListener(v -> getActivity().onBackPressed());

        b.btnDelete.setOnClickListener(v -> delete());

        b.btnSave.setOnClickListener(v -> {
            if (noteId != -1) {
                update();
                return;
            }
            save(new Note(b.edtTitle.getText().toString().trim(), b.edtContent.getText().toString().trim()));
        });
    }

    private void delete() {
        BellilerDatabase.databaseWriteExecutor.execute(() -> {
            daoNote.delete(noteId);

            if (getActivity() == null) return;

            getActivity().runOnUiThread(() -> getActivity().onBackPressed());
        });
    }


    private void update() {
        BellilerDatabase.databaseWriteExecutor.execute(() -> {
            daoNote.updateValues(b.edtTitle.getText().toString().trim(), b.edtContent.getText().toString().trim(), noteId);

            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> getActivity().onBackPressed());
        });
    }

    private void save(Note notes) {
        b.edtTitle.setText("");
        b.edtContent.setText("");

        BellilerDatabase.databaseWriteExecutor.execute(() -> daoNote.insert(notes));
    }
}