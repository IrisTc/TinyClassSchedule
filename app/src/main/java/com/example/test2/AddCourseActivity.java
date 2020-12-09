package com.example.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test2.adapter.CourseAdapter;
import com.example.test2.model.Course;
import com.example.test2.service.course_database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AddCourseActivity extends AppCompatActivity {
    EditText etCourse;
    EditText etTeacher;
    EditText etSectionLeft;
    EditText etSectionRight;
    TimePicker etTimeLeft;
    TimePicker etTimeRight;
    Spinner spWeek;
    Button btAdd;
    Button btSubmit;
    Integer week_int = 0;
    String name_str;
    String teacher_str;
    Integer sectionleft_int = 0;
    Integer sectionRight_int = 0;
    String time_str_left = "";
    String time_str_right = "";
    String time_str;
    ListView lvTemp;
    List<Course> courseList;
    CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        courseList = new ArrayList<>();
        getData();
        findView();

        courseAdapter = new CourseAdapter(AddCourseActivity.this, courseList);
        lvTemp.setAdapter(courseAdapter);
        setListViewHeightBasedOnChildren(lvTemp, courseAdapter);

        final String[] weekList = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        ArrayAdapter<String> wekkAdapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_list_item_1, weekList);
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

        lvTemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(AddCourseActivity.this)
                        .setTitle("提示")
                        .setMessage("确定要删除该项吗")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing - it will close on its own

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = AddCourseActivity.this.getSharedPreferences("course_add_temp", AddCourseActivity.this.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove(courseList.get(position).getName());
                                editor.apply();
                                refresh();
                            }
                        })
                        .show();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str = etCourse.getText().toString();
                teacher_str = etTeacher.getText().toString();
                time_str = time_str_left + " - " + time_str_right;

                if (time_str_left.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "请选择开始时间！", Toast.LENGTH_LONG).show();
                } else {
                    if (time_str_right.equals("")) {
                        Toast.makeText(AddCourseActivity.this, "请选择结束时间！", Toast.LENGTH_LONG).show();
                    } else {
                        if (name_str.equals("") || teacher_str.equals("") || etSectionLeft.getText().toString().equals("")|| etSectionRight.getText().toString().equals("")) {
                            Toast.makeText(AddCourseActivity.this, "请填写完整信息！", Toast.LENGTH_LONG).show();
                        } else {
                            sectionleft_int = Integer.parseInt(etSectionLeft.getText().toString());
                            sectionRight_int = Integer.parseInt(etSectionRight.getText().toString());

                            //可以创建一个新的SharedPreference来对储存的文件进行操作
                            SharedPreferences sp = AddCourseActivity.this.getSharedPreferences("course_add_temp", AddCourseActivity.this.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            Course temp = new Course(name_str, teacher_str, sectionleft_int, sectionRight_int, time_str, week_int);
                            editor.putString(temp.getName(), temp.toString());
                            editor.apply();
                            refresh();
                        }
                    }
                }
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_database service = new course_database(AddCourseActivity.this);
                SQLiteDatabase sqLiteDatabase = service.getWritableDatabase();

                for (int i = 0; i < courseList.size(); i++) {
                    Course item = courseList.get(i);
                    service.add(sqLiteDatabase, item.getName(), item.getTeacher(), item.getSection_left(), item.getSection_right(), item.getTime(), item.getWeek());
                }
                sqLiteDatabase.close();

                SharedPreferences sp = AddCourseActivity.this.getSharedPreferences("course_add_temp", AddCourseActivity.this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refresh() {
        courseList.clear();
        getData();
        courseAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lvTemp, courseAdapter);
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("course_add_temp", MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) sharedPreferences.getAll();
        Set<Map.Entry<String, String>> entrys = map.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            String value = entry.getValue();
            String[] fields = value.split("&");
            courseList.add(new Course(fields[0], fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]), fields[4], Integer.parseInt(fields[5])));
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView, CourseAdapter adapter) {
        // 获取ListView对应的Adapter
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void findView() {
        lvTemp = findViewById(R.id.lv_temp);
        etCourse = findViewById(R.id.et_course);
        etTeacher = findViewById(R.id.et_teacher);
        etSectionLeft = findViewById(R.id.et_section_left);
        etSectionRight = findViewById(R.id.et_section_right);
        etTimeLeft = findViewById(R.id.et_time_left);
        etTimeRight = findViewById(R.id.et_time_right);
        spWeek = findViewById(R.id.sp_Week);
        btAdd = findViewById(R.id.bt_add);
        btSubmit = findViewById(R.id.bt_submit);
    }
}
