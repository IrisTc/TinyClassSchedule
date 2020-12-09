package com.example.test2.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.test2.model.Course;

import java.util.ArrayList;
import java.util.List;

public class course_database extends SQLiteOpenHelper {
    public course_database(@Nullable Context context) {
        super(context, "course_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table course(id integer primary key autoincrement,name varchar(128),teacher varchar(20),section_left Integer,section_right Integer,time varchar(64), week Integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add(SQLiteDatabase sqLiteDatabase, String name, String teacher, Integer section_left, Integer section_right, String time, Integer week) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("teacher", teacher);
        values.put("section_left", section_left);
        values.put("section_right", section_right);
        values.put("time", time);
        values.put("week", week);
        sqLiteDatabase.insert("course", null, values);
    }

    public void edit(SQLiteDatabase sqLiteDatabase, Integer course_id, String name, String teacher, Integer section_left, Integer section_right, String time, Integer week) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("teacher", teacher);
        values.put("section_left", section_left);
        values.put("section_right", section_right);
        values.put("time", time);
        values.put("week", week);
        sqLiteDatabase.update("course", values, "id=?", new String[]{course_id.toString()});
    }

    public List<Course> queryByWeek(SQLiteDatabase sqLiteDatabase, Integer day) {
        Cursor cursor = sqLiteDatabase.query("course", null, "week=?", new String[]{day.toString()}, null, null, "section_left ASC");
        List<Course> list = new ArrayList<Course>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(1);
            String teacher = cursor.getString(2);
            Integer section_left = cursor.getInt(3);
            Integer section_right = cursor.getInt(4);
            String time = cursor.getString(5);
            int week = cursor.getInt(6);
            list.add(new Course(id, name,teacher,section_left,section_right,time,week));
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<Course> queryById(SQLiteDatabase sqLiteDatabase, Integer course_id) {
        Cursor cursor = sqLiteDatabase.query("course", null, "id=?", new String[]{course_id.toString()}, null, null, "section_left ASC");
        List<Course> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(1);
            String teacher = cursor.getString(2);
            Integer section_left = cursor.getInt(3);
            Integer section_right = cursor.getInt(4);
            String time = cursor.getString(5);
            int week = cursor.getInt(6);
            list.add(new Course(id, name,teacher,section_left,section_right,time,week));
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public void delete(SQLiteDatabase sqLiteDatabase, Integer id) {
        //删除条件
        String whereClause = "id=?";
        //删除条件参数
        String[] whereArgs = {String.valueOf(id)};
        //执行删除
        sqLiteDatabase.delete("course",whereClause,whereArgs);
        sqLiteDatabase.close();
    }
}
