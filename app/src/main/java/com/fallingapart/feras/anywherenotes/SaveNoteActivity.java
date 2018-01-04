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
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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
    private boolean headingPanelExpanded = false;

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
        findViewById(R.id.editor_controls).setVisibility(ViewGroup.GONE);
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

        // undo redo
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

        // general text
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
        findViewById(R.id.btn_editor_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setUnderline();
            }
        });
        findViewById(R.id.btn_editor_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setStrikeThrough();
            }
        });

        // alignment
        findViewById(R.id.btn_editor_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignLeft();
            }
        });
        findViewById(R.id.btn_editor_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignCenter();
            }
        });
        findViewById(R.id.btn_editor_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignRight();
            }
        });
        findViewById(R.id.btn_editor_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setIndent();
            }
        });
        findViewById(R.id.btn_editor_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setOutdent();
            }
        });

        // headings
        findViewById(R.id.editor_controls_headings).setVisibility(View.GONE);
        findViewById(R.id.btn_editor_heading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHeadingsPanel();
            }
        });

        findViewById(R.id.btn_editor_heading_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(1);
                toggleHeadingsPanel();
            }
        });
        findViewById(R.id.btn_editor_heading_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(2);
                toggleHeadingsPanel();
            }
        });
        findViewById(R.id.btn_editor_heading_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(3);
                toggleHeadingsPanel();
            }
        });
        findViewById(R.id.btn_editor_heading_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(4);
                toggleHeadingsPanel();
            }
        });
        findViewById(R.id.btn_editor_heading_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(5);
                toggleHeadingsPanel();
            }
        });
        findViewById(R.id.btn_editor_heading_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(6);
                toggleHeadingsPanel();
            }
        });

        // bullets and lists
        findViewById(R.id.btn_editor_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBullets();
            }
        });
        findViewById(R.id.btn_editor_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setNumbers();
            }
        });

        // misc

        findViewById(R.id.btn_editor_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setSuperscript();
            }
        });
        findViewById(R.id.btn_editor_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setSubscript();
            }
        });
        findViewById(R.id.btn_editor_quote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBlockquote();
            }
        });
    }

    private void toggleHeadingsPanel() {
        View headings = findViewById(R.id.editor_controls_headings);
        if (headingPanelExpanded) {
            collapse(headings);
            headingPanelExpanded = !headingPanelExpanded;
        } else {
            ((HorizontalScrollView)findViewById(R.id.editor_controls_scroller)).smoothScrollTo(0,0);
            expand(headings);
            headingPanelExpanded = !headingPanelExpanded;
        }
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final int targetWidth = v.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().width = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetWidth * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialWidth = v.getMeasuredWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().width = initialWidth - (int)(initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
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
