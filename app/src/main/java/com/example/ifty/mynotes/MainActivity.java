package com.example.ifty.mynotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.example.ifty.mynotes.adapters.NotesAdapter;
import com.example.ifty.mynotes.datadases.NotesDatabaseManager;
import com.example.ifty.mynotes.models.Note;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NotesDatabaseManager databaseManager;
    ArrayList<Note>notes;
    RecyclerView noteView;
    NotesAdapter notesAdapter;
    int viewMode = 0;
    ImageView viewModeIcon;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        databaseManager=new NotesDatabaseManager(this);
        notes=databaseManager.getAllNotes();
        Collections.reverse(notes);

        noteView =findViewById(R.id.noteView);
        viewModeIcon=findViewById(R.id.viewModeIcon);

        sharedPreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        viewMode = sharedPreferences.getInt("viewMode",0);

        if (viewMode==0){
            viewModeIcon.setImageResource(R.drawable.ic_list_black_24dp);
            noteView.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
            notesAdapter=new NotesAdapter(this,notes,R.layout.custom_gridview);
            noteView.setAdapter(notesAdapter);
        }
        else {
            viewModeIcon.setImageResource(R.drawable.ic_view_module_black_24dp);
            noteView.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL));
            notesAdapter=new NotesAdapter(this,notes,R.layout.custom_listview);
            noteView.setAdapter(notesAdapter);
        }

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void newNote(View view) {
        Intent intent = new Intent(this,NewNoteActivity.class);
        intent.putExtra("newOrViewNote","new note");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void viewMode(View view) {
        if (viewMode==0){
            viewMode=1;
            viewModePreferenceSave(viewMode);
            Intent intent = new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            viewMode=0;
            viewModePreferenceSave(viewMode);
            Intent intent = new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private void viewModePreferenceSave(int viewMode) {
        editor = sharedPreferences.edit();
        editor.putInt("viewMode", viewMode);
        editor.apply();
    }
}
