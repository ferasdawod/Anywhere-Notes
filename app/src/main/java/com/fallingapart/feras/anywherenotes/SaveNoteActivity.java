package com.fallingapart.feras.anywherenotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fallingapart.feras.anywherenotes.Models.Note;

import java.util.Date;

import jp.wasabeef.richeditor.RichEditor;

public class SaveNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";

    EditText txtTitle;
    RadioGroup noteColors;
    FloatingActionButton fab;
    RichEditor richEditor;

    private long _noteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNoteActivity.this.SaveNote();
            }
        });*/

        txtTitle = (EditText)findViewById(R.id.txtNoteTitle);
        noteColors = (RadioGroup)findViewById(R.id.note_colors);

        richEditor = (RichEditor)findViewById(R.id.note_editor);
        setupRichEditor();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_NOTE_ID)) {
            long noteId = intent.getLongExtra(EXTRA_NOTE_ID, 0);
            if (noteId != 0) {
                _noteId = noteId;
                Note note = Note.findById(Note.class, noteId);

                txtTitle.setText(note.Name);
                richEditor.setHtml(note.Description);
                noteColors.check(getRadioButtonIdFromColorId(note.ColorId));
                getSupportActionBar().setTitle(R.string.edit_note);
            } else {
                getSupportActionBar().setTitle(R.string.new_note);
            }
        } else {
            getSupportActionBar().setTitle(R.string.new_note);
        }

        //if (intent != null && intent.hasExtra(EXTRA_FROM_NOTIFICATION) && intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)) {
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //} else {
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //}
    }

    private void setupRichEditor() {
        richEditor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        richEditor.setPlaceholder("Note Content...");
        richEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.editor_controls).setVisibility(ViewGroup.VISIBLE);
                } else {
                    findViewById(R.id.editor_controls).setVisibility(ViewGroup.GONE);
                }
            }
        });

        findViewById(R.id.btn_editor_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.undo();
            }
        });
        findViewById(R.id.btn_editor_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.redo();
            }
        });
        findViewById(R.id.btn_editor_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBold();
            }
        });
        findViewById(R.id.btn_editor_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setItalic();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_btn_save_note) {
            SaveNote();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        String description = richEditor.getHtml();

        if (name.length() == 0) {
            Toast.makeText(this, R.string.title_validation, Toast.LENGTH_LONG).show();
            txtTitle.setError(getString(R.string.title_validation));
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
