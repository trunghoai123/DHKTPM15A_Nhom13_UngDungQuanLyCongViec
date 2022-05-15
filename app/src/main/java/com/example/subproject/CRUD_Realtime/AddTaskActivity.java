package com.example.subproject.CRUD_Realtime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.subproject.R;

import java.util.Calendar;
import java.util.HashMap;

public class AddTaskActivity extends AppCompatActivity {
    //date
    DatePickerDialog datePicker;

    //time
    TimePickerDialog timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_position = findViewById(R.id.edit_position);
        final EditText edit_date = findViewById(R.id.edit_date);
        final EditText edit_time = findViewById(R.id.edit_time);
        Button btn = findViewById(R.id.btn_submit);
        Button btn_open = findViewById(R.id.btn_open);

        // date
        edit_date.setInputType(InputType.TYPE_NULL);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(AddTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edit_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });
        // Gio
        edit_time .setInputType(InputType.TYPE_NULL);
        edit_time .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(AddTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                edit_time .setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                timePicker.show();
            }
        });

        btn_open.setOnClickListener(v->
        {
            Intent intent =new Intent(AddTaskActivity.this, RVActivity.class);
            startActivity(intent);
        });
        DAOEmployee dao =new DAOEmployee();
        CongViec emp_edit = (CongViec)getIntent().getSerializableExtra("EDIT");
        if(emp_edit !=null)
        {
            btn.setText("UPDATE");
            edit_name.setText(emp_edit.getTitle());
            edit_position.setText(emp_edit.getContent());
            edit_date.setText(emp_edit.getDate());
            edit_time.setText(emp_edit.getTime());
            btn_open.setVisibility(View.GONE);
        }
        else
        {
            btn.setText("ADD");
            btn_open.setVisibility(View.VISIBLE);
        }
        btn.setOnClickListener(v->
        {
            CongViec emp = new CongViec(edit_name.getText().toString(), edit_position.getText().toString(), edit_date.getText().toString(),edit_time.getText().toString());
            if(emp_edit==null)
            {
                dao.add(emp).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "Add thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            else
            {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", edit_name.getText().toString());
                hashMap.put("content", edit_position.getText().toString());
                hashMap.put("date", edit_date.getText().toString());
                hashMap.put("time", edit_time.getText().toString());
                dao.update(emp_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(AddTaskActivity.this, RVActivity.class);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


    }
}