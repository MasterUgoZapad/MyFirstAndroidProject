package com.example.biokm_000.scino_library;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by biokm_000 on 12.08.2015.
 */
public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        RadioGroup radiogroupSort = (RadioGroup) findViewById(R.id.RadioGroupSort);
        switch (MainActivity.mSort){
            case 0:
                RadioButton btn1 = (RadioButton)findViewById(R.id.radioDefault);
                btn1.toggle();
                break;
            case 1:
                RadioButton btn2 = (RadioButton)findViewById(R.id.radioName);
                btn2.toggle();
                break;
            case 2:
                RadioButton btn3 = (RadioButton)findViewById(R.id.radioAutor);
                btn3.toggle();
                break;
            case 3:
                RadioButton btn4 = (RadioButton)findViewById(R.id.radioJenre);
                btn4.toggle();
                break;
        }
        switch (MainActivity.mDirection) {
            case 0:
                RadioButton btn1 = (RadioButton) findViewById(R.id.radioUp);
                btn1.toggle();
                break;
            case 1:
                RadioButton btn2 = (RadioButton) findViewById(R.id.radioDown);
                btn2.toggle();
                break;
        }
        radiogroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioDefault:
                        MainActivity.mSort = 0;
                        break;
                    case R.id.radioName:
                        MainActivity.mSort = 1;
                        break;
                    case R.id.radioAutor:
                        MainActivity.mSort = 2;
                        break;
                    case R.id.radioJenre:
                        MainActivity.mSort = 3;
                        break;
                    default:
                        break;
                }
            }
        });
        RadioGroup radiogroupDirection = (RadioGroup) findViewById(R.id.RadioGroupDirection);
        radiogroupDirection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioUp:
                        MainActivity.mDirection = 0;
                        break;
                    case R.id.radioDown:
                        MainActivity.mDirection = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void OnCancelSearchButtonClick(View view) {
        finish();
    }

    public void OnSearchButtonClick(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.OptionSpinner);
        TextView text = (TextView)findViewById(R.id.SearchText);
        String searchText = text.getText().toString();
        switch (spinner.getSelectedItemPosition()){
            case 0: MainActivity.mPreviousSearchParameter = BookDatabase.NAME + " LIKE \"%" + searchText + "%\" OR " + BookDatabase.AUTOR+ " LIKE \"%" + searchText + "%\" OR " + BookDatabase.JENRE + " LIKE \"%" + searchText + "%\"";
                break;
            case 1: MainActivity.mPreviousSearchParameter = BookDatabase.NAME + " LIKE \"%" + searchText + "%\"" ;
                break;
            case 2: MainActivity.mPreviousSearchParameter = BookDatabase.AUTOR + " LIKE \"%" + searchText + "%\"" ;
                break;
            case 3: MainActivity.mPreviousSearchParameter = BookDatabase.JENRE + " LIKE \"%" + searchText + "%\"";
                break;
        }

        finish();
    }
}
