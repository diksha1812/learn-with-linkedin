package com.example.extra.myapplication.DataClasses;

public class Course {
    String course_name;
    String course_description;
    int course_id;
    int topic_id;

    public Course(){}

    public int getTopicId() {
        return topic_id;
    }

    public void setTopicId(int topic) {
        this.topic_id = topic_id;
    }

    public String getCourseName(){
        return course_name;
    }
    public String getCourseDescription(){
        return course_description;
    }
    public int getCourseId(){
        return course_id;
    }

    public void setCourseName(String course_name){
        this.course_name = course_name;
    }
    public void setCourseDescription(String course_description){
        this.course_description = course_description;
    }
    public void setCourseId(int course_id){
        this.course_id = course_id;
    }
}
