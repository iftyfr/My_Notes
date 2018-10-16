package com.example.ifty.mynotes.datadases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="myNoteDB";
    public static final int DATABASE_VERSION=3;

    public static final String NOTES_TABLE="myNotes";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_NOTE="note";
    public static final String COLUMN_DATE_N_TIME="dateNtime";
    public static final String COLUMN_PHOTO_PATH="photoPath";

    public static final String CREATE_NOTES_TABLE="create table "+NOTES_TABLE+"("+COLUMN_ID+" integer primary key autoincrement,"+COLUMN_TITLE+" text,"+COLUMN_NOTE+" text,"+COLUMN_DATE_N_TIME+" text,"+COLUMN_PHOTO_PATH+" text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+NOTES_TABLE);
        onCreate(sqLiteDatabase);
    }
}
