package com.example.test2;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.test2.model.Course;
import com.example.test2.service.course_database;

import java.util.List;

public class LeftFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.left_fragment_layout, container, false);

        ListView lvWeek = root.findViewById(R.id.lv_week);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //初始化第一项选中
                View v = lvWeek.getChildAt(0);
                v.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            }
        }, 500);


        lvWeek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    View v = lvWeek.getChildAt(0);
                    v.setBackgroundResource(0);
                }
                course_database courses = new course_database(getActivity());
                SQLiteDatabase sqLiteDatabase = courses.getReadableDatabase();
                List<Course> dataList = courses.queryByWeek(sqLiteDatabase, position);
                RightFragment content = (RightFragment) ((MainActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.right_fragment);
                content.setText(dataList, position);
            }
        });

        return root;
    }
}
