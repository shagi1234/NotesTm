package com.merw_okuw_merkezi.bellikler.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.merw_okuw_merkezi.bellikler.AdapterNotes;
import com.merw_okuw_merkezi.bellikler.databinding.FragmentHomeBinding;
import com.merw_okuw_merkezi.bellikler.db.BellilerDatabase;
import com.merw_okuw_merkezi.bellikler.db.DaoNote;
import com.merw_okuw_merkezi.bellikler.model.Note;

import java.util.List;

public class FragmentHome extends Fragment {
    //viewBinding ulandyk
    private FragmentHomeBinding b;
    private AdapterNotes adapterNotes;
    private DaoNote daoNote;

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoNote = BellilerDatabase.getBellilerDatabase(getContext()).daoNote();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false);
        setRecycler();
        getAllNotes();
        initListeners();

        return b.getRoot();
    }

    private void initListeners() {
        if (getActivity() == null) return;

        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this::getAllNotes);

        b.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BellilerDatabase.databaseWriteExecutor.execute(() -> {
                    List<Note> notes = daoNote.searchNotes(s.toString().trim());
                    if (getActivity() == null) return;

                    getActivity().runOnUiThread(() -> adapterNotes.setNotes(notes));
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getAllNotes();
    }

    private void getAllNotes() {
        BellilerDatabase.databaseWriteExecutor.execute(() -> {
            List<Note> notes = daoNote.getAllNotes();
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> adapterNotes.setNotes(notes));
        });

    }

    private void setRecycler() {
        adapterNotes = new AdapterNotes(getContext(), getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 4 == 0 || position % 4 == 1) {
                    return 1;
                }
                return 2;
            }
        });
        b.recNotes.setLayoutManager(gridLayoutManager);
        b.recNotes.setAdapter(adapterNotes);
    }
}