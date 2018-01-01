package com.fallingapart.feras.anywherenotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fallingapart.feras.anywherenotes.Models.Note;

import java.text.SimpleDateFormat;

public class ShowNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    private TextView _lblTitle;
    private TextView _lblDescription;
    private TextView _lblDate;

    private ViewGroup _mainContent;
    Toolbar _toolbar;

    private long _noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _lblTitle = (TextView)findViewById(R.id.lblNoteTitle);
        _lblDescription = (TextView)findViewById(R.id.lblNoteDescription);
        _lblDate = (TextView)findViewById(R.id.lblNoteDate);
        _mainContent = (ViewGroup)findViewById(R.id.content_show_note);

        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            final long noteId = intent.getLongExtra(EXTRA_NOTE_ID, 0);
            if (noteId != 0) {
                _noteId = noteId;
                Note note = Note.findById(Note.class, noteId);
                bindNote(note);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(ShowNoteActivity.this, SaveNoteActivity.class);
                        editIntent.putExtra(SaveNoteActivity.EXTRA_NOTE_ID, noteId);
                        startActivityForResult(editIntent, 0);
                    }
                });
            } else {
                // TODO show alert
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_actionmode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.delete)
                    .setMessage(R.string.delete_confirmation_s)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Note note = Note.findById(Note.class, _noteId);
                            note.delete();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Note note = Note.findById(Note.class, _noteId);
        bindNote(note);
    }

    private void bindNote(Note note) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");

        _lblTitle.setText(note.Name);
        _lblDescription.setText(Html.fromHtml(note.Description));
        _lblDate.setText(dateFormat.format(note.UpdatedAt));

        int noteColor = getResources().getColor(note.ColorId);
        _mainContent.setBackgroundColor(noteColor);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (note.ColorId == R.color.note_color_0) {
            _toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            _lblTitle.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            _lblDescription.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            _lblDate.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        } else {
            _toolbar.setBackgroundColor(manipulateColor(noteColor, 0.7f));
            fab.setBackgroundTintList(ColorStateList.valueOf(manipulateColor(noteColor, 0.7f)));
            _lblTitle.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            _lblDescription.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            _lblDate.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        }
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
