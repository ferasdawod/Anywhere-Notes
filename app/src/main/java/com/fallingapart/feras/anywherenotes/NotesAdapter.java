package com.fallingapart.feras.anywherenotes;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fallingapart.feras.anywherenotes.Models.Note;

import java.text.SimpleDateFormat;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private List<Note> _notes;
    private MainActivity _parentActivity;

    public NotesAdapter(List<Note> notes, MainActivity parentActivity) {
        if (notes == null)
            throw new NullPointerException("notes");

        this._notes = notes;
        this._parentActivity = parentActivity;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
        return new NotesViewHolder(inflatedView, _parentActivity);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Note note = _notes.get(position);
        holder.bindNote(note);
    }

    @Override
    public int getItemCount() {
        return _notes.size();
    }

    public void updateDataSource(List<Note> notes) {
        _notes = notes;
        notifyDataSetChanged();
    }
}
