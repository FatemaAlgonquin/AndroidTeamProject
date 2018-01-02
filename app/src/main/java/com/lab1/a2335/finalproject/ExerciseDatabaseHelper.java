package com.lab1.a2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by fatema on 2017-11-18.
 */

public class ExerciseDatabaseHelper extends SQLiteOpenHelper {
    protected static final String ACTIVITY_NAME = "ExerciseDatabaseHelper";
    protected static final String DATABASE_NAME = "Activity105.db";
    protected static final int VERSION_NUMBER = 1;

    protected static final String TABLE_NAME = "ACTIVITY_TABLE";
    public static final String KEY_ACTIVITY = "Activity";
    protected static final String KEY_ID = "id";
    public static final String KEY_COMMENT = "Comment";
    public static final String KEY_ACTIVITY_DURATION = "Duration";
    public static final String KEY_ACTIVITY_DATE = "SelectedDate";

    private final Context mctx;
    SQLiteDatabase mdb;

    public static final String CREATE_TABLE_ACTIVITY =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_ACTIVITY + " TEXT, "
                    + KEY_COMMENT + " TEXT, "
                    + KEY_ACTIVITY_DURATION + " INTEGER, "
                    + KEY_ACTIVITY_DATE + " TEXT"
                    +");";

    public static final String[] ACTIVITY_FIELDS = new String[]{
            KEY_ID,
            KEY_ACTIVITY,
            KEY_COMMENT,
            KEY_ACTIVITY_DURATION,
            KEY_ACTIVITY_DATE
    };


    public ExerciseDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.mctx = context;
    }



    // only creating the table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ACTIVITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(ACTIVITY_NAME, "Upgrading database from version "+ oldVersion + " to " + newVersion + ", which " +
                "will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    // open database
    public ExerciseDatabaseHelper open() {
        if(mdb == null){
            mdb = getWritableDatabase();
        }

        return this;
    }

    public void close(){
        if(mdb != null){
            mdb.close();
        }
    }

    // retrieving data
    public Cursor getChatMessages(){
        return mdb.query(TABLE_NAME, ACTIVITY_FIELDS, null, null, null, null, null);
    }

    public Cursor getActivity(String activityName){
        return mdb.query(TABLE_NAME,ACTIVITY_FIELDS,KEY_ACTIVITY+"=?",new String[] { activityName.toLowerCase() },null,null,null);
    }

    public Cursor getActivitiesByMonth(String dateAsString){
        String query = "SELECT * FROM " + TABLE_NAME  + " WHERE strftime('%Y-%m', " + KEY_ACTIVITY_DATE+") = strftime('%Y-%m','" + dateAsString+"')";
        Cursor cursor =   mdb.rawQuery(query,null);
        return cursor;
    }


    public int getTotalActivityByMonth(String dateAsString){
        int total = -1;
        String query = "SELECT SUM("+KEY_ACTIVITY_DURATION+") FROM " + TABLE_NAME  + " WHERE strftime('%Y-%m', " + KEY_ACTIVITY_DATE+") = strftime('%Y-%m','" + dateAsString+"')";
        Cursor cursor =   mdb.rawQuery(query,null);
        if(cursor.moveToFirst()){
            total = cursor.getInt(0);
        }
        return total;
    }


    public String getMessageFromCursor(Cursor cursor){
        String msg = cursor.getString(cursor.getColumnIndex(KEY_COMMENT));
        return msg;

    }

    public int getIdFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        return id;

    }

    public void insert(ContentValues content){
        mdb.insert(TABLE_NAME, null, content);

    }

    public void remove(long id){
        //String s_id;
        //s_id = String.valueOf(id);
        // mdb.execSQL("DELETE FROM "  + CHAT_TABLE  + " WHERE _id = " + id);
        //mdb.delete(CHAT_TABLE, KEY_ID, +" = ?", new String[] { s_id });


        //mdb.execSQL("DELETE FROM " + CHAT_TABLE + " WHERE " + KEY_ID + "= " + id);

        int deletedRecrod =  mdb.delete(TABLE_NAME, "id" + "=" + id, null);
        Log.i("Deleted ",Integer.toString(deletedRecrod));
    }
}
