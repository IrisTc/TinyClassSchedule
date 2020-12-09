package com.example.test2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test2.model.Course;
import com.example.test2.service.course_database;

import java.util.List;

public class EditCourseActivity extends AppCompatActivity {
    EditText etCourse;
    EditText etTeacher;
    EditText etSectionLeft;
    EditText etSectionRight;
    TimePicker etTimeLeft;
    TimePicker etTimeRight;
    TextView tvTime;
    Spinner spWeek;
    Button btSubmit;
    Integer week_int;
    String name_str;
    String teacher_str;
    Integer sectionleft_int = 0;
    Integer sectionRight_int = 0;
    String time_str_left = "";
    String time_str_right = "";
    String time_str;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findView();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        course_database service = new course_database(EditCourseActivity.this);
        SQLiteDatabase sqLiteDatabase = service.getWritableDatabase();
        List<Course> dataList = service.queryById(sqLiteDatabase, id);

        Course course = dataList.get(0);

        System.out.println(course.getName());

        name_str = course.getName();
        teacher_str = course.getTeacher();
        sectionleft_int = course.getSection_left();
        sectionRight_int = course.getSection_right();
        time_str = course.getTime();

        etCourse.setText(name_str);
        etTeacher.setText(teacher_str);
        etSectionLeft.setText(sectionleft_int.toString());
        etSectionRight.setText(sectionRight_int.toString());
        tvTime.setText(time_str);

        final String[] weekList = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        ArrayAdapter<String> wekkAdapter = new ArrayAdapter<String>(EditCourseActivity.this, android.R.layout.simple_list_item_1, weekList);
        spWeek.setAdapter(wekkAdapter);

        spWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                week_int = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etTimeLeft.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time_str_left = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            }
        });

        etTimeRight.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time_str_right = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str = etCourse.getText().toString();
                teacher_str = etTeacher.getText().toString();

                if (!time_str_left.equals("") && !time_str_right.equals("")) {
                    time_str = time_str_left + " - " + time_str_right;
                } else {
                    if (name_str.equals("") || teacher_str.equals("") || etSectionLeft.getText().toString().equals("") || etSectionRight.getText().toString().equals("")) {
                        Toast.makeText(EditCourseActivity.this, "请填写完整信息！", Toast.LENGTH_LONG).show();
                    } else {
                        sectionleft_int = Integer.parseInt(etSectionLeft.getText().toString());
                        sectionRight_int = Integer.parseInt(etSectionRight.getText().toString());

                        course_database service = new course_database(EditCourseActivity.this);
                        SQLiteDatabase sqLiteDatabase = service.getWritableDatabase();
                        service.edit(sqLiteDatabase, id, name_str, teacher_str, sectionleft_int, sectionRight_int, time_str, week_int);

                        Intent intent = new Intent(EditCourseActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void findView() {
        etCourse = findViewById(R.id.et_course);
        etTeacher = findViewById(R.id.et_teacher);
        etSectionLeft = findViewById(R.id.et_section_left);
        etSectionRight = findViewById(R.id.et_section_right);
        etTimeLeft = findViewById(R.id.et_time_left);
        etTimeRight = findViewById(R.id.et_time_right);
        spWeek = findViewById(R.id.sp_Week);
        tvTime = findViewById(R.id.tv_time);
        btSubmit = findViewById(R.id.bt_submit);
    }
}
