package com.example.biokm_000.scino_library;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biokm_000 on 14.08.2015.
 */
public class BookDatabase extends SQLiteOpenHelper implements BaseColumns {

    public final static String GROUP_TABLE_NAME = "GroupTable";
    public final static String BOOK_TABLE_NAME = "BookTable";
    public final static String DATABASE_NAME = "MyDatabase.db";
    public final static Integer DATABASE_VERSION = 1;
    public final static String NAME = "name";
    public final static String AUTOR = "autor";
    public final static String JENRE = "jenre";
    public final static String READ = "readed";
    public final static String GROUP = "category";
    public final static String GROUP_ID = "groupId";
    public final static String BOOK_ID = "bookId";
    public final static String DEFAULT_GROUP_NAME = "UnGrouped";
    Context mContext;

    public BookDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    public BookDatabase(Context context,  SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;
    }
    public BookDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + GROUP_TABLE_NAME + " ( " + GROUP_ID + " integer primary key autoincrement, " + GROUP + " text)");
        db.execSQL("create table " + BOOK_TABLE_NAME + " ( " + BOOK_ID + " integer primary key autoincrement," + NAME + " text, " + AUTOR + " text, " + JENRE + " text, " + READ + " integer, " + GROUP_ID + " integer, FOREIGN KEY (" + GROUP_ID + ") REFERENCES " + GROUP_TABLE_NAME + "(" + GROUP_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        db.execSQL("DROP TABLE " + GROUP_TABLE_NAME);
        db.execSQL("DROP TABLE " + BOOK_TABLE_NAME);
        onCreate(db);
    }
    public ArrayList<MyBook> GetParamList(String Param, SQLiteDatabase mSqLiteDatabase, String tableName, String searchParam, String groupParam, String readParam, Integer sort, Integer direction){

        ArrayList<MyBook> BooksList = new ArrayList<>();
        String query = Param + tableName;
        if (searchParam.length() == 0)
            query+= " WHERE ("+ NAME+ " LIKE \"%\")";
        else
            query+=" WHERE ("+searchParam+")";
        if (groupParam.length() == 0)
            query+= " AND ("+ GROUP_ID+ " LIKE \"%\")";
        else
            query+=" AND ("+groupParam+")";
        if (readParam.length() == 0)
            query+= " AND ("+ READ+ " LIKE \"%\")";
        else
            query+=" AND ("+readParam+")";
        query += " ORDER BY ";
        switch (sort) {
            case 0:
                query+= BOOK_ID;
                break;
            case 1:
                query+= NAME;
                break;
            case 2:
                query+= AUTOR;
                break;
            case 3:
                query+= JENRE;
                break;
        }
        if (direction == 1)
            query+=" DESC";
        Cursor cursor = mSqLiteDatabase.rawQuery( query, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0)
            do {
                MyBook book = new MyBook(cursor.getInt(cursor.getColumnIndex(BookDatabase.BOOK_ID)),
                        cursor.getString(cursor.getColumnIndex(BookDatabase.NAME)),
                        cursor.getString(cursor.getColumnIndex(BookDatabase.AUTOR)),
                        cursor.getString(cursor.getColumnIndex(BookDatabase.JENRE)),
                        cursor.getInt(cursor.getColumnIndex(BookDatabase.READ)),
                        cursor.getInt(cursor.getColumnIndex(BookDatabase.GROUP_ID)));
                BooksList.add(book);
            } while (cursor.moveToNext());
            cursor.close();
            return BooksList;
    }
    public ArrayList<MyGroup> GetParamGroupList(SQLiteDatabase mSqLiteDatabase) {

        ArrayList<MyGroup> GroupList = new ArrayList<MyGroup>();
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT * FROM " + BookDatabase.GROUP_TABLE_NAME, null);
        if (cursor.moveToNext())
            do {
                String name = cursor.getString(cursor.getColumnIndex(BookDatabase.GROUP));
                Integer id = cursor.getInt(cursor.getColumnIndex(BookDatabase.GROUP_ID));
                GroupList.add(new MyGroup(id,name));
            } while (cursor.moveToNext());
        cursor.close();
        return GroupList;
    }
    public void ChangeGridParametrizedDB( String parameter, String searchParameter, String groupParameter, String readParameter, Integer Sort, Integer Direction) {
        MainActivity.mPreviousParameter = parameter;
        MainActivity.mPreviousSearchParameter = searchParameter;
        MainActivity.mPreviousGroupParameter = groupParameter;
        MainActivity.mPreviousReadParameter = readParameter;
        ArrayList<MyBook> BooksList = MainActivity.mBookDatabase.GetParamList(parameter, MainActivity.mSqLiteDatabase, BookDatabase.BOOK_TABLE_NAME, searchParameter, groupParameter, readParameter, Sort, Direction);
        ListView listView = (ListView) MainActivity.activity.findViewById(R.id.BookListView);
        BookAdapter adapter= new BookAdapter(MainActivity.activity,BooksList);
        listView.setAdapter(adapter);
    }
    public void  ChangeGridParametrizedDB( ) {
        ChangeGridParametrizedDB(MainActivity.mPreviousParameter, MainActivity.mPreviousSearchParameter, MainActivity.mPreviousGroupParameter, MainActivity.mPreviousReadParameter, MainActivity.mSort, MainActivity.mDirection);
    }
}
