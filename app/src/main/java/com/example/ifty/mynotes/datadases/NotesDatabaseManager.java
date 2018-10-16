package com.example.ifty.mynotes.datadases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ifty.mynotes.models.Note;

import java.util.ArrayList;

public class NotesDatabaseManager {

    DatabaseHelper databaseHelper;
    Context context;

    public NotesDatabaseManager(Context context) {
        this.context = context;
        databaseHelper= new DatabaseHelper(context);
    }

    public long addNote(Note note){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
        contentValues.put(DatabaseHelper.COLUMN_NOTE, note.getNote());
        contentValues.put(DatabaseHelper.COLUMN_DATE_N_TIME, note.getDateNtime());
        contentValues.put(DatabaseHelper.COLUMN_PHOTO_PATH, note.getPhotoPath());

        long insert =sqLiteDatabase.insert(DatabaseHelper.NOTES_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return insert;
    }
    public long updateNote(Note note){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
        contentValues.put(DatabaseHelper.COLUMN_NOTE, note.getNote());
        contentValues.put(DatabaseHelper.COLUMN_DATE_N_TIME, note.getDateNtime());
        contentValues.put(DatabaseHelper.COLUMN_PHOTO_PATH, note.getPhotoPath());


        long update =sqLiteDatabase.update(DatabaseHelper.NOTES_TABLE,contentValues,DatabaseHelper.COLUMN_ID+"=?",new String[]{ String.valueOf(note.getId())});
        sqLiteDatabase.close();
        return update;
    }
    public ArrayList<Note>getAllNotes(){
        ArrayList<Note>noteList=new ArrayList<>();
        String selectQuery = "select * from "+DatabaseHelper.NOTES_TABLE;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                String note = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE));
                String dateNtime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_N_TIME));
                String photoPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTO_PATH));
                Note notes = new Note(id,title,note,dateNtime,photoPath);
                noteList.add(notes);
            }while (cursor.moveToNext());
        }
        return noteList;
    }

    public Note singleNote (int id){
        Note note=null;
        String selectQuery = "select * from "+DatabaseHelper.NOTES_TABLE+" where id="+id;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
            String notes = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE));
            String dateNtime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_N_TIME));
            String photoPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTO_PATH));
            note = new Note(id,title,notes,dateNtime,photoPath);
        }
        return note;
    }

    public long deleteNote(int id){
        SQLiteDatabase sqLiteDatabase=databaseHelper.getWritableDatabase();
        long delete = sqLiteDatabase.delete(DatabaseHelper.NOTES_TABLE,DatabaseHelper.COLUMN_ID+"=?",new String[]{String.valueOf(id)});
        return delete;
    }
}
