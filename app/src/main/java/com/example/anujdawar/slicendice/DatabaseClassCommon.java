package com.example.anujdawar.slicendice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseClassCommon extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "SliceIce.db";
    public static final String TABLE_NAME = "syrup_details";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "AVAILABILITY";
    public static final String COL_4 = "DESCRIPTION";

    public DatabaseClassCommon(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY,NAME TEXT,AVAILABILITY TEXT,DESCRIPTION TEXT)");
    }

    public boolean insertData(Integer key, String NameOfSyrup, String ifAvailable, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, String.valueOf(key));
        contentValues.put(COL_2, NameOfSyrup);
        contentValues.put(COL_3, ifAvailable);
        contentValues.put(COL_4, description);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor viewAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public boolean updateData(String id, String NameOfSyrup, String ifAvailable, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, NameOfSyrup);
        contentValues.put(COL_3, ifAvailable);
        contentValues.put(COL_4, description);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});

        return true;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }
}