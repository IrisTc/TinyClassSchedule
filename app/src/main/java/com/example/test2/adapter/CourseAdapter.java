package com.example.test2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test2.R;
import com.example.test2.model.Course;

import java.util.List;

public class CourseAdapter extends BaseAdapter {
    Context context;
    private List<Course> list;

    public CourseAdapter(Context context, List<Course> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_item_layout, parent, false);

        Course course = list.get(position);

        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvTeacher = view.findViewById(R.id.tv_teacher);
        TextView tvSection = view.findViewById(R.id.tv_section);
        TextView tvTime = view.findViewById(R.id.tv_time);

        tvName.setText(course.getName());
        tvTeacher.setText(course.getTeacher());
        tvSection.setText(course.getSection_left() + " - " + course.getSection_right());
        tvTime.setText(course.getTime());

        return view;
    }
}