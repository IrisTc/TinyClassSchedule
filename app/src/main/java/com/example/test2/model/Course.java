package com.example.test2.model;

public class Course {
    private Integer id;
    private String name;
    private String teacher;
    private Integer section_left;
    private Integer section_right;
    private String time;
    private Integer week;

    public Course(Integer id, String name, String teacher, Integer section_left, Integer section_right, String time, Integer week) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.section_left = section_left;
        this.section_right = section_right;
        this.time = time;
        this.week = week;
    }

    public Course(String name, String teacher, Integer section_left, Integer section_right, String time, Integer week) {
        this.name = name;
        this.teacher = teacher;
        this.section_left = section_left;
        this.section_right = section_right;
        this.time = time;
        this.week = week;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Integer getSection_left() {
        return section_left;
    }

    public void setSection_left(Integer section_left) {
        this.section_left = section_left;
    }

    public Integer getSection_right() {
        return section_right;
    }

    public void setSection_right(Integer section_right) {
        this.section_right = section_right;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return name + '&' +  teacher + '&' + section_left + '&' + section_right + '&' + time + '&' + week;
    }
}
