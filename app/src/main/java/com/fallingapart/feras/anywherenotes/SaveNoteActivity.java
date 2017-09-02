package com.fallingapart.feras.anywherenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fallingapart.feras.anywherenotes.Models.Note;

import java.util.Date;

public class SaveNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    EditText txtTitle;
    EditText txtDescription;
    RadioGroup noteColors;
    FloatingActionButton fab;

    private long _noteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNoteActivity.this.SaveNote();
            }
        });

        txtTitle = (EditText)findViewById(R.id.txtNoteTitle);
        txtDescription = (EditText)findViewById(R.id.txtNoteDescription);
        noteColors = (RadioGroup)findViewById(R.id.note_colors);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_NOTE_ID)) {
            long noteId = intent.getLongExtra(EXTRA_NOTE_ID, 0);
            if (noteId != 0) {
                _noteId = noteId;
                Note note = Note.findById(Note.class, noteId);

                txtTitle.setText(note.Name);
                txtDescription.setText(note.Description);
                noteColors.check(getRadioButtonIdFromColorId(note.ColorId));
            }
        }
    }

    private int getRadioButtonIdFromColorId(int colorId) {
        switch (colorId) {
            case R.color.note_color_0: return R.id.note_color_0;
            case R.color.note_color_1: return R.id.note_color_1;
            case R.color.note_color_2: return R.id.note_color_2;
            case R.color.note_color_3: return R.id.note_color_3;
            case R.color.note_color_4: return R.id.note_color_4;
            case R.color.note_color_5: return R.id.note_color_5;
            case R.color.note_color_6: return R.id.note_color_6;
            case R.color.note_color_7: return R.id.note_color_7;
            default: return -1;
        }
    }

    private int getColorIdFromRadioButtonId(int radioButtonId) {
        switch (radioButtonId) {
            case R.id.note_color_0: return R.color.note_color_0;
            case R.id.note_color_1: return R.color.note_color_1;
            case R.id.note_color_2: return R.color.note_color_2;
            case R.id.note_color_3: return R.color.note_color_3;
            case R.id.note_color_4: return R.color.note_color_4;
            case R.id.note_color_5: return R.color.note_color_5;
            case R.id.note_color_6: return R.color.note_color_6;
            case R.id.note_color_7: return R.color.note_color_7;
            default: return 0;
        }
    }

    private void SaveNote() {
        String name = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        final Note note;
        if (_noteId == 0)
            note = new Note();
        else
            note = Note.findById(Note.class, _noteId);

        note.Name = name;
        note.Description = description;
        note.UpdatedAt = new Date();
        note.ColorId = getColorIdFromRadioButtonId(noteColors.getCheckedRadioButtonId());
        note.save();

        Toast.makeText(this, R.string.note_saved, Toast.LENGTH_LONG).show();
        finish();

        /*Snackbar.make(fab, "Note Saved", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        note.delete();
                    }
                }).show();*/
    }

}
