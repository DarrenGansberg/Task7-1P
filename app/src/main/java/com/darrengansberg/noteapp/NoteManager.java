package com.darrengansberg.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;

import com.darrengansberg.noteapp.data.NoteDatabaseHelper;
import com.darrengansberg.noteapp.models.Note;
import com.darrengansberg.noteapp.util.Util;

/*Android specific NoteManager implementation */
/* Responsible for managing the notes of the application by
performing data access operations (persistence and retrieval of notes).
 */
public class NoteManager {

    private boolean databaseClosed = true;
    private SQLiteDatabase noteDatabase = null;

    public void openDatabaseReadOnly(@NonNull Context context){

        if (databaseClosed)
        {
            NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
            noteDatabase = dbHelper.getReadableDatabase();
            databaseClosed = false;
        } else if (!noteDatabase.isReadOnly()) {
            //the database is open, but in the wrong (i.e. writeable) model,
            //so it will be closed and reopened in the correct mode.
            closeDatabase();
            openDatabaseReadOnly(context);
        }
    }

    public void openDatabase(@NonNull Context context){

        //Open the database in writable mode
        if (databaseClosed)
        {
            NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
            noteDatabase = dbHelper.getWritableDatabase();
            databaseClosed = false;
        } else if (noteDatabase.isReadOnly())
        {
            //the database is open, but in the wrong mode (i.e. readonly, and not writeable),
            // so it needs to be closed, and reopened in writable mode.
            closeDatabase();
            openDatabase(context);
        }
    }

    public void closeDatabase()
    {
        if (databaseClosed){
            return; //nothing to do
        }
        noteDatabase.close();
        databaseClosed = true;
    }

    public void add(@NonNull Context context, @NonNull Note newNote)
    {
        // only close the database if it wasn't already open.
        //allows the user to manually control open and close of database for perf. reasons.
        boolean closeDatabase = false;
        if (databaseClosed)
        {
            //the database will be opened by this call, set this to true to
            // ensure it is closed upon exit.
            closeDatabase = true;
            openDatabase(context);
        }
        addNewNoteQuery(newNote);
        if (closeDatabase)
        {
            noteDatabase.close();
        }
    }

   private void addNewNoteQuery(Note newNote)
   {
       ContentValues values = new ContentValues();
       values.put(Util.NOTE_CONTENT, newNote.getContent());
       long result = noteDatabase.insertOrThrow(Util.NOTES_TABLE_NAME,null,values);
       if (result == -1)
       {
           throw new IllegalStateException("Adding note to database failed");
       }
       newNote.setId(result);
   }

   public int getCount(@NonNull Context context){

        boolean closedDb = false;
        if (databaseClosed)
        {
            closedDb = true;
            openDatabaseReadOnly(context);
        }

        String query = String.format("SELECT COUNT(%1$s) FROM " + Util.NOTES_TABLE_NAME, Util.NOTE_ID);
        Cursor cursor =  noteDatabase.rawQuery(query,null);
        if(!cursor.moveToFirst())
        {
            throw new IllegalStateException("Could not retrieve number of notecards");
        }
        int result = cursor.getInt(0);
        cursor.close();
        if (closedDb)
        {
            closeDatabase();
        }
        return result;
   }
   public Note getById(@NonNull Context context, long noteId)
   {
       boolean closedDb = false;
       if (databaseClosed)
       {
           closedDb = true;
           openDatabaseReadOnly(context);
       }
       Note result = getNote(noteId);
       if (closedDb)
       {
           closeDatabase();
       }
       return result;
   }

   public Note getByIndex(@NonNull Context context, int noteIndex)
   {
       boolean closedDb = false;
       if (databaseClosed)
       {
           closedDb = true;
           openDatabaseReadOnly(context);
       }
       Note result = getByIndex(noteIndex);
       if (closedDb)
       {
           closeDatabase();
       }
       return result;
   }

   private Note getByIndex(int index)
   {
       String[] columns = new String[]{ Util.NOTE_ID, Util.NOTE_CONTENT };
       Cursor cursor = noteDatabase.query(Util.NOTES_TABLE_NAME, columns, null, null,
               null,null,Util.NOTE_ID, null);
       if(!cursor.moveToPosition(index))
       {
           throw new IllegalArgumentException("Could not find note");
       }
       Note result = new Note();
       result.setId(cursor.getLong(Util.NOTE_ID_INDEX));
       result.setContent(cursor.getString(Util.NOTE_CONTENT_ID));
       return result;
   }

   private Note getNote(long noteId)
   {
       String criteria = String.format("%1$s = ?", Util.NOTE_ID);
       String[] args =  new String[]{ String.valueOf(noteId) };
       int limit = 1; //there should only be one note with the noteId, as it is the PK.
       Cursor cursor = noteDatabase.query(Util.NOTES_TABLE_NAME, null, criteria, args,
               null,null,null, String.valueOf(limit));
       Note result = new Note();
       if (!cursor.moveToFirst())
           throw new IllegalStateException("Failed to retrieve note");
       result.setId(cursor.getLong(Util.NOTE_ID_INDEX));
       result.setContent(cursor.getString(Util.NOTE_CONTENT_ID));
       cursor.close();
       return result;
   }

    public void delete(@NonNull Context context, long noteId) {

        boolean closeDb = false;
        if (databaseClosed)
        {
            closeDb = true;
            openDatabase(context);
        }
        String criteria = Util.NOTE_ID + "=?";
        String[] args = new String[]{ String.valueOf(noteId) };
        int result = noteDatabase.delete(Util.NOTES_TABLE_NAME,criteria,args);
        if (result != 1)
        {
            throw new IllegalArgumentException("Could not delete the note");
        }
        if (closeDb)
        {
            closeDatabase();
        }


    }

    public void update(@NonNull Context context, @NonNull Note note)
    {
        boolean closeDb = false;
        if (databaseClosed)
        {
            closeDb = true;
            openDatabase(context);
        }
        ContentValues values = new ContentValues();
        values.put(Util.NOTE_CONTENT, note.getContent());
        String criteria = Util.NOTE_ID + "=?";
        String[] args = new String[]{ String.valueOf(note.getId()) };
        int result = noteDatabase.update(Util.NOTES_TABLE_NAME, values, criteria, args);
        if (result != 1)
        {
            throw new IllegalStateException("Failed to save note update");
        }
        if (closeDb)
        {
            closeDatabase();
        }

    }
}
