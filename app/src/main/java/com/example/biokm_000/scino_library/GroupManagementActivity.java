package com.example.biokm_000.scino_library;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biokm_000 on 12.08.2015.
 */
public class GroupManagementActivity extends Activity {
    private ArrayList<MyGroup> mGroupArray;
    private Integer mMode = -1;
    private EditText mText;
    private Button mButton1;
    private Button mButton2;
    private ArrayList<Button> myButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_management_activity);
        mText = (EditText)findViewById(R.id.TextGroupName);
        mButton1 = (Button)findViewById(R.id.buttonProceed);
        mButton2 = (Button)findViewById(R.id.buttonCancel);
        myButtons = new ArrayList<>();
        myButtons.add((Button)findViewById(R.id.buttonAddGroup));
        myButtons.add((Button)findViewById(R.id.button19));
        myButtons.add((Button) findViewById(R.id.button20));
        myButtons.add((Button)findViewById(R.id.button21));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroupArray = ChangeSpinnerDB();
    }

    private void show(){
        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        for(int i=0;i<myButtons.size();++i)
            myButtons.get(i).setEnabled(false);
    }
    private void hide(){
        mText.setText("");
        mText.setVisibility(View.INVISIBLE);
        mButton1.setVisibility(View.INVISIBLE);
        mButton2.setVisibility(View.INVISIBLE);
        for(int i=0;i<myButtons.size();++i)
            myButtons.get(i).setEnabled(true);
    }
    public void OnAddGroupButtonClick(View view) {
        mMode = 0;
        mText.setVisibility(View.VISIBLE);
        show();
    }

    public void OnDeleteGroupButtonClick(View view) {
        mMode = 2;
        show();
    }

    public void OnRenameGroupButtonClick(View view) {
        mMode = 1;
        mText.setVisibility(View.VISIBLE);
        show();
    }

    public void OnCancelMngButtonClick(View view) {
        finish();
    }
    public void OnProseedButtonClick(View view){
        Spinner spinner = (Spinner) findViewById(R.id.GroupSpinner);
        switch (mMode){
            case 0:
                ContentValues newValues = new ContentValues();
                if (mText.getText().length()>0) {
                    Cursor cursor = MainActivity.mSqLiteDatabase.rawQuery("SELECT * FROM " + BookDatabase.GROUP_TABLE_NAME + " WHERE " + BookDatabase.GROUP + " = \"" + mText.getText().toString() + "\"", null);
                    if (cursor.getCount() <= 0) {
                        newValues.put(BookDatabase.GROUP, mText.getText().toString());
                        MainActivity.mSqLiteDatabase.insert(MainActivity.mBookDatabase.GROUP_TABLE_NAME, null, newValues);
                        hide();
                        mGroupArray = ChangeSpinnerDB();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.WarningGroupExistence), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 1:

                if (mText.getText().length() > 0) {
                    MainActivity.mSqLiteDatabase.execSQL("UPDATE " + BookDatabase.GROUP_TABLE_NAME + " SET " + BookDatabase.GROUP + " = \"" + mText.getText().toString() + "\" WHERE " + BookDatabase.GROUP + " = \"" + spinner.getSelectedItem().toString() + "\"");
                    hide();
                    mGroupArray = ChangeSpinnerDB();
                }
                break;
            case 2:
                if (spinner.getCount() > 0) {
                    for (Integer i = 0; i < mGroupArray.size(); ++i)
                        if (mGroupArray.get(i).GetString() == spinner.getSelectedItem().toString()) {
                            MainActivity.mSqLiteDatabase.execSQL("UPDATE " + BookDatabase.BOOK_TABLE_NAME + " SET " + BookDatabase.GROUP_ID + " = 1 WHERE " + BookDatabase.GROUP_ID + " = " + mGroupArray.get(i).GetID());
                            break;
                        }
                    MainActivity.mSqLiteDatabase.execSQL("DELETE FROM " + BookDatabase.GROUP_TABLE_NAME + " WHERE " + BookDatabase.GROUP + " = \"" + spinner.getSelectedItem().toString() + "\"");
                    hide();
                    mGroupArray = ChangeSpinnerDB();
                }
                else{
                        Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.WarningGroupUnExistence), Toast.LENGTH_SHORT);
                        toast.show();
                }

                break;

    }
    }
    public void OnBackButtonClick(View view) {
        hide();
    }


    public ArrayList<MyGroup> ChangeSpinnerDB(){
        ArrayList<MyGroup> GroupList = MainActivity.mBookDatabase.GetParamGroupList(MainActivity.mSqLiteDatabase);
        ArrayList<String> GroupStringList = new ArrayList<String>();
        for (Integer i = 0;i<GroupList.size();++i)
            if (!GroupList.get(i).GetString().equals( BookDatabase.DEFAULT_GROUP_NAME))
                GroupStringList.add(GroupList.get(i).GetString());
        Spinner spinner = (Spinner) findViewById(R.id.GroupSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GroupStringList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        return GroupList;
    }
}
