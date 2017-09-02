package com.fallingapart.feras.anywherenotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallingapart.feras.anywherenotes.Models.Note;

import java.text.SimpleDateFormat;

/**
 * Created by ASUS on 2/9/2017.
 */

public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView DateTextView;
    public TextView TitleTextView;
    public ViewGroup NoteRootColor;

    private ImageView _checkedImageView;
    private Note _note;
    private MainActivity _parentActivity;

    private boolean _isSelected = false;

    public Note getNote() { return _note; }
    public boolean getIsSelected() { return _isSelected; }

    public NotesViewHolder(View view, MainActivity parentActivity) {
        super(view);
        this._parentActivity = parentActivity;

        TitleTextView = (TextView)view.findViewById(R.id.lblNoteTitle);
        DateTextView = (TextView)view.findViewById(R.id.lblNoteDate);
        NoteRootColor = (ViewGroup)view.findViewById(R.id.note_item_root_color);
        _checkedImageView = (ImageView)view.findViewById(R.id.note_check_state);

        View v = view.findViewById(R.id.notes_item_root);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
    }

    public void bindNote(Note note) {
        _note = note;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        this.NoteRootColor.setBackgroundColor(_parentActivity.getResources().getColor(note.ColorId));

        this.TitleTextView.setText(note.Name);
        this.DateTextView.setText(dateFormat.format(note.UpdatedAt));
        if (note.ColorId == R.color.note_color_0) {
            TitleTextView.setTextColor(_parentActivity.getResources().getColor(android.R.color.primary_text_light));
            DateTextView.setTextColor(_parentActivity.getResources().getColor(android.R.color.primary_text_light));
        } else {
            TitleTextView.setTextColor(_parentActivity.getResources().getColor(android.R.color.primary_text_dark));
            DateTextView.setTextColor(_parentActivity.getResources().getColor(android.R.color.primary_text_dark));
        }
    }

    @Override
    public void onClick(View v) {
        _parentActivity.onNoteClick(this);

    }

    @Override
    public boolean onLongClick(View v) {
        _parentActivity.onNoteLongClick(this);
        return true;
    }

    public void toggleSelection() {
        setSelected(!_isSelected);
    }

    public void setSelected(boolean isSelected) {
        _isSelected = isSelected;
        if (_isSelected) {
            _checkedImageView.setVisibility(View.VISIBLE);
            //TitleTextView.setPadding(TitleTextView.getPaddingLeft(), 2, TitleTextView.getPaddingRight(), TitleTextView.getExtendedPaddingBottom());
        } else {
            _checkedImageView.setVisibility(View.GONE);
            //TitleTextView.setPadding(TitleTextView.getPaddingLeft(), 26, TitleTextView.getPaddingRight(), TitleTextView.getExtendedPaddingBottom());
        }
    }
}