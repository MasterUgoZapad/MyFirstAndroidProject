package com.example.biokm_000.scino_library;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    public static Activity activity;
    public static BookDatabase mBookDatabase;
    public static SQLiteDatabase mSqLiteDatabase;
    public static String mPreviousParameter = "SELECT * FROM ";
    public static String mPreviousSearchParameter = "";
    public static String mPreviousGroupParameter = "";
    public static String mPreviousReadParameter = "";
    public static ArrayList<MyGroup> mGroupArray;
    public static Integer mSort = 0;
    public static Integer mDirection = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookDatabase = new BookDatabase(this, null);
        mSqLiteDatabase = mBookDatabase.getWritableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery( "SELECT * FROM " + mBookDatabase.GROUP_TABLE_NAME, null);
        if (cursor.getCount() == 0) {
            ContentValues newValues = new ContentValues();
            newValues.put(BookDatabase.GROUP, BookDatabase.DEFAULT_GROUP_NAME);
            newValues.put(BookDatabase.GROUP_ID, 1);
            mSqLiteDatabase.insert(MainActivity.mBookDatabase.GROUP_TABLE_NAME, null, newValues);
        }
        Spinner spinner = (Spinner) findViewById(R.id.GroupSpinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBookDatabase.ChangeGridParametrizedDB();
        mGroupArray = ChangeSpinnerDB();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_books:
                Intent intent1 = new Intent(MainActivity.this,AddBookActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_groups:
                Intent intent2 = new Intent(MainActivity.this,GroupManagementActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void OnButtonAddTextClick(View view) {
        Intent intent = new Intent(MainActivity.this,AddBookActivity.class);
        startActivity(intent);
    }

    public void OnSearchButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    public void OnGroupButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this,GroupManagementActivity.class);
        startActivity(intent);
    }


    public ArrayList<MyGroup> ChangeSpinnerDB(){
        ArrayList<MyGroup> GroupList = mBookDatabase.GetParamGroupList(mSqLiteDatabase);
        ArrayList<String> GroupStringList = new ArrayList<String>();
        GroupStringList.add(getString(R.string.SpinnerAllItem));
        for (Integer i = 0;i<GroupList.size();++i)
            GroupStringList.add(GroupList.get(i).GetString());
        Spinner spinner = (Spinner) findViewById(R.id.GroupSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GroupStringList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        return GroupList;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).toString() != getString(R.string.SpinnerAllItem)) {
            for (Integer i = 0; i < mGroupArray.size(); ++i)
                if (mGroupArray.get(i).GetString() == parent.getItemAtPosition(position).toString()) {
                    mPreviousGroupParameter = BookDatabase.GROUP_ID + " = " + mGroupArray.get(i).GetID().toString();
                    mBookDatabase.ChangeGridParametrizedDB();
                    break;
                }
        } else {
            mPreviousGroupParameter = BookDatabase.GROUP_ID + " LIKE \"%\"";
            mBookDatabase.ChangeGridParametrizedDB();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void OnCheckBoxClick(View view) {
        CheckBox read = (CheckBox)findViewById(R.id.checkBoxRead);
        CheckBox unread = (CheckBox)findViewById(R.id.checkBoxUnread);
        if (read.isChecked() && unread.isChecked())
            mBookDatabase.ChangeGridParametrizedDB(mPreviousParameter, mPreviousSearchParameter, mPreviousGroupParameter, "",mSort,mDirection);
        else
            if (!read.isChecked() && unread.isChecked())
                mBookDatabase.ChangeGridParametrizedDB(mPreviousParameter, mPreviousSearchParameter, mPreviousGroupParameter, BookDatabase.READ + " = 0",mSort,mDirection);
        else
            if (read.isChecked() && !unread.isChecked())
                mBookDatabase.ChangeGridParametrizedDB(mPreviousParameter, mPreviousSearchParameter, mPreviousGroupParameter, BookDatabase.READ + " = 1",mSort,mDirection);
        else
                mBookDatabase.ChangeGridParametrizedDB(mPreviousParameter, mPreviousSearchParameter, mPreviousGroupParameter, BookDatabase.READ + " = NULL",mSort,mDirection);
    }

    public void OnNullButtonClick(View view) {
        mPreviousSearchParameter = "" ;
        mBookDatabase.ChangeGridParametrizedDB();
    }
}
