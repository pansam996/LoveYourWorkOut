package com.example.workout_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQL_DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WorkOut.db";
    private static final String TABLE_NAME = "RecordTable";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "DATE";
    private static final String COL_3 = "SPORT";
    private static final String COL_4 = "SPORTTOOL";
    private static final String COL_5 = "TOTALWEIGHT";
    private static final String COL_6 = "RECORD";

    private Context con;

    public SQL_DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                ",DATE TEXT,SPORT TEXT,SPORTTOOL TEXT, TOTALWEIGHT INTEGER,RECORD TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insertData(long x ,int y){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("xValues",x);
        contentValues.put("yValues",y);

        db.insert("MyTable",null,contentValues);
    }

    public boolean insert_Data(String date,String sport,String sporttool,int totalweight,String record){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,date);
        contentValues.put(COL_3,sport);
        contentValues.put(COL_4,sporttool);
        contentValues.put(COL_5,totalweight);
        contentValues.put(COL_6,record);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else return true;

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getTraingDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT DATE from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getSportName(String part){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT SPORTTOOL from "+TABLE_NAME+" where SPORT=?",new String[]{part});
        return res;
    }

    public Cursor getDateRecord(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+COL_1+","+COL_4+","+COL_5+","+COL_6+" from "+TABLE_NAME+" WHERE DATE=?",new String[]{date});
        return res;
    }

    public Cursor getDatePoint(String date,String SportTool){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DATE , TOTALWEIGHT  from "+TABLE_NAME+" WHERE DATE LIKE ? AND SPORTTOOL =?",new String[]{date+"%",SportTool});
        return res;
    }

    public Cursor getSportRecord(String date,String SportTool){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select RECORD,TOTALWEIGHT  from "+TABLE_NAME+" WHERE DATE =?"+" AND SPORTTOOL =?",new String[]{date,SportTool});
        return res;
    }

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{id});
    }

    public Integer deleteSomeData (String date){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"DATE = ?",new String[]{date});
    }


}
