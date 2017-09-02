package com.fallingapart.feras.anywherenotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.fallingapart.feras.anywherenotes.Models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView _notesList;
    private RecyclerView.LayoutManager _layoutManager;
    private NotesAdapter _notesAdapter;
    private ActionMode _actionMode;

    private ViewGroup _emptyView;

    ArrayList<NotesViewHolder> _selectedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveNoteActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        _emptyView = (ViewGroup)findViewById(R.id.list_empty);

        setupRecyclerView();
        showNotification();
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, SaveNoteActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.click_to_add_note));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setOngoing(true);
        builder.setOnlyAlertOnce(true);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(0, notification);
    }


    @Override
    protected void onResume() {
        updateData();
        super.onResume();
    }

    private void setupRecyclerView() {
        _notesList = (RecyclerView)findViewById(R.id.notes_recyclerView);
        _layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        _notesList.setLayoutManager(_layoutManager);

        _notesAdapter = new NotesAdapter(new ArrayList<Note>(), this);
        _notesList.setAdapter(_notesAdapter);
        updateData();
    }

    private void updateData() {
        List<Note> notes = Note.listAll(Note.class);
        _notesAdapter.updateDataSource(notes);

        if (notes.size() == 0) {
            _emptyView.setVisibility(View.VISIBLE);
        } else {
            _emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNoteClick(NotesViewHolder viewHolder) {
        if (_actionMode != null) {
            viewHolder.toggleSelection();

            if (viewHolder.getIsSelected()){
                _selectedNotes.add(viewHolder);
            } else {
                _selectedNotes.remove(viewHolder);
            }

            updateActionModeStatus();

        } else {
            Intent intent = new Intent(this, ShowNoteActivity.class);
            intent.putExtra(SaveNoteActivity.EXTRA_NOTE_ID, viewHolder.getNote().getId());
            startActivity(intent);
        }
    }

    private void updateActionModeStatus() {
        if (_actionMode == null) return;

        if (_selectedNotes.size() == 0) {
            _actionMode.finish();
        } else {
            _actionMode.setTitle(_selectedNotes.size() + " Notes Selected");
        }
    }

    public void onNoteLongClick(NotesViewHolder viewHolder) {
        if (_actionMode != null) return;

        _selectedNotes = new ArrayList<>();
        _selectedNotes.add(viewHolder);
        viewHolder.toggleSelection();
        _actionMode = startSupportActionMode(new android.support.v7.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main_actionmode_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.menu_action_delete:
                        handleDeleteNotes();
                        return true;
                }
                return false;
            }

            private void handleDeleteNotes() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Notes")
                        .setMessage("Do you really want to delete " + _selectedNotes.size() + " Notes ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (NotesViewHolder selectedNote : _selectedNotes) {
                                    Note.delete(selectedNote.getNote());
                                }
                                _actionMode.finish();
                                updateData();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null).show();
            }

            @Override
            public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
                for (NotesViewHolder selectedNote : _selectedNotes) {
                    selectedNote.setSelected(false);
                }
                _actionMode = null;
            }
        });
        updateActionModeStatus();
    }
}
