package com.example.extra.myapplication.DataClasses;

public class LessonInfo {
    int course_id;
    int lesson_no;
    String lesson_name;
    String lesson_desc;

    public LessonInfo(){}

    public int getCourse_id() {
        return course_id;
    }

    public int getLesson_no() {
        return lesson_no;
    }

    public String getLesson_desc() {
        return lesson_desc;
    }

    public String getLesson_name() {
        return lesson_name;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public void setLesson_desc(String lesson_desc) {
        this.lesson_desc = lesson_desc;
    }

    public void setLesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public void setLesson_no(int lesson_no) {
        this.lesson_no = lesson_no;
    }
}
