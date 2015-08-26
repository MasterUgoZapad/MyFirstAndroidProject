package com.example.biokm_000.scino_library;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class AddBookActivity extends Activity {
    public static ArrayList<MyGroup> mGroupArray;
    public static Integer mBookId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_activity);
        mBookId = getIntent().getIntExtra(BookDatabase.BOOK_ID, -1);
        EditText et1 = (EditText)findViewById(R.id.TextName);
        EditText et3 = (EditText)findViewById(R.id.TextAutor);
        EditText et2 = (EditText)findViewById(R.id.TextGenre);
        if (mBookId != -1) {
            Cursor cursor = MainActivity.mSqLiteDatabase.rawQuery("SELECT * FROM " + BookDatabase.BOOK_TABLE_NAME + " WHERE " + BookDatabase.BOOK_ID + " = \"" + mBookId.toString() + "\"", null);
            cursor.moveToFirst();
            MyBook book = new MyBook(cursor.getInt(cursor.getColumnIndex(BookDatabase.BOOK_ID)),
                    cursor.getString(cursor.getColumnIndex(BookDatabase.NAME)),
                    cursor.getString(cursor.getColumnIndex(BookDatabase.AUTOR)),
                    cursor.getString(cursor.getColumnIndex(BookDatabase.JENRE)),
                    cursor.getInt(cursor.getColumnIndex(BookDatabase.READ)),
                    cursor.getInt(cursor.getColumnIndex(BookDatabase.GROUP_ID)));
            cursor.close();
            et1.setText(book.GetName());
            et2.setText(book.GetAutor());
            et3.setText(book.GetJenre());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroupArray = ChangeSpinnerDB();
    }

    public void OnCancelButtonClick(View view) {
        finish();
    }
    public void OnButtonAddClick(View view) {
        EditText et1 = (EditText)findViewById(R.id.TextName);
        EditText et2 = (EditText)findViewById(R.id.TextAutor);
        EditText et3 = (EditText)findViewById(R.id.TextGenre);
        MyBook book;
        Cursor cursor = MainActivity.mSqLiteDatabase.rawQuery("SELECT * FROM " + BookDatabase.BOOK_TABLE_NAME + " WHERE (" + BookDatabase.NAME + " = \"" + et1.getText().toString() + "\") AND (" + BookDatabase.JENRE + " = \"" + et3.getText().toString() + "\") AND (" + BookDatabase.AUTOR + " = \"" + et2.getText().toString() + "\")", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            book = new MyBook(cursor.getInt(cursor.getColumnIndex(BookDatabase.BOOK_ID)),
                    cursor.getString(cursor.getColumnIndex(BookDatabase.NAME)),
                    cursor.getString(cursor.getColumnIndex(BookDatabase.AUTOR)),
                    cursor.getString(cursor.getColumnIndex(BookDatabase.JENRE)),
                    cursor.getInt(cursor.getColumnIndex(BookDatabase.READ)),
                    cursor.getInt(cursor.getColumnIndex(BookDatabase.GROUP_ID)));
        }
        else
            book = null;
        cursor.close();
        if ((et1.getText().length() > 0) &&  (et2.getText().length() > 0) && (et3.getText().length() > 0)) {
            if ((mBookId != -1) || ((mBookId == -1) && (book == null))) {
                Integer read;
                if (book != null)
                    read = book.GetRead();
                else
                    read = 0;
                ContentValues newValues = new ContentValues();
                newValues.put(BookDatabase.NAME, (et1.getText().toString()));
                newValues.put(BookDatabase.AUTOR, (et2.getText().toString()));
                newValues.put(BookDatabase.JENRE, (et3.getText().toString()));
                newValues.put(BookDatabase.READ, read);

                Spinner spinner = (Spinner) findViewById(R.id.GroupSpinner);
                String GroupName = spinner.getSelectedItem().toString();
                for (Integer i = 0; i < mGroupArray.size(); ++i)
                    if (mGroupArray.get(i).GetString() == GroupName) {
                        newValues.put(BookDatabase.GROUP_ID, mGroupArray.get(i).GetID());
                        break;
                    }
                if (mBookId != -1) {
                    newValues.put(BookDatabase.BOOK_ID, mBookId);
                    MainActivity.mSqLiteDatabase.replace(MainActivity.mBookDatabase.BOOK_TABLE_NAME, null, newValues);
                } else
                    MainActivity.mSqLiteDatabase.insert(MainActivity.mBookDatabase.BOOK_TABLE_NAME, null, newValues);
                finish();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.WarningExistence), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.WarningNotEnoughData), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public ArrayList<MyGroup> ChangeSpinnerDB() {
        ArrayList<MyGroup> GroupList = MainActivity.mBookDatabase.GetParamGroupList(MainActivity.mSqLiteDatabase);
        ArrayList<String> GroupStringList = new ArrayList<String>();
        for (Integer i = 0; i < GroupList.size(); ++i)
            GroupStringList.add(GroupList.get(i).GetString());
        Spinner spinner = (Spinner) findViewById(R.id.GroupSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GroupStringList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        return GroupList;

    }
}
