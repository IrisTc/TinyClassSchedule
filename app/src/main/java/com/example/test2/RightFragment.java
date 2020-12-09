package com.example.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.test2.adapter.CourseAdapter;
import com.example.test2.model.Course;
import com.example.test2.service.course_database;

import java.util.ArrayList;
import java.util.List;

public class RightFragment extends Fragment {
    private View root;
    private List<Course> dataList;
    private ListView lvCourse;
    private CourseAdapter adapter;
    TextView test;
    Integer week;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化星期一的数据
        week = 0;
        dataList = new ArrayList<>();
        getData(0);

        //List绑定适配器
        lvCourse = root.findViewById(R.id.lv_course);
        adapter = new CourseAdapter(getActivity(), dataList);
        lvCourse.setAdapter(adapter);

        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("请选择操作")
                        .setNegativeButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing - it will close on its own
                                Intent intent = new Intent(getActivity(), EditCourseActivity.class);
                                intent.putExtra("id", dataList.get(position).getId());
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                course_database service = new course_database(getActivity());
                                SQLiteDatabase sqLiteDatabase = service.getWritableDatabase();
                                service.delete(sqLiteDatabase, dataList.get(position).getId());
                                getData(week);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
    }

    private void getData(int i) {
        course_database courses = new course_database(getActivity());
        SQLiteDatabase sqLiteDatabase = courses.getReadableDatabase();
        dataList.clear();
        dataList.addAll(courses.queryByWeek(sqLiteDatabase, i));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.right_fragment_layout, container, false);
        return root;
    }

    public void setText(List<Course> list, Integer i) {
        week = i;
        dataList.clear();
        dataList.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
