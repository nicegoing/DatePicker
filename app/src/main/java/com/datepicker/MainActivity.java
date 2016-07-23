package com.datepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvShow = (TextView) findViewById(R.id.tv_show);

    }

    public void onClick(View v){
        DatePickerDialog datePickerDialog= DatePickerDialog.newInstance();
        datePickerDialog.show(getSupportFragmentManager(),"datePickerDialog");
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void selectDate(int year, int month, int day) {
                tvShow.setText(year+" "+(month+1)+" "+day);
            }
        });
    }

    public void onClickTwo(View v){
        android.app.DatePickerDialog datePickerDialog=new android.app.DatePickerDialog(this, 0,new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        },0,0,0);
        datePickerDialog.show();
    }

}
