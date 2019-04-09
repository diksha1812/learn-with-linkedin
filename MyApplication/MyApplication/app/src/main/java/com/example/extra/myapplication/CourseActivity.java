package com.example.extra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.extra.myapplication.Adapters.adapter_courses_recom;
import com.example.extra.myapplication.Adapters.adapter_rv_profile;
import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.DataClasses.LessonInfo;
import com.example.extra.myapplication.Fragments.RecomFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity implements adapter_rv_profile.adapter_rv_profile_listener{

    ImageButton ib;
    int flag;
    RecyclerView recyclerView, recyclerView_courses;
    RecyclerView.Adapter adapter, adapter_courses;
    RecyclerView.LayoutManager layoutManager, layoutManager_courses;
    LinearLayout parent;
    String topic_id, course_id, course_name;
    List<String> course_list;
/*
    private void fetchCourses(String keyword) {
        JSONObject jsonObject = new JSONObject();
        String url = "https://finalapi.herokuapp.com/api";
        //String url = "https://linkedinapikavita.herokuapp.com/api";
        try {
            jsonObject.put("course_name", keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "BEFORE REQUEST");
        if(isNetworkAvailable()){

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                    url,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("RESPONSE FROM SERVER", response.toString());
                    List<Course> course_list = new ArrayList<>();
                    try {
                        int size = response.getJSONArray("Courses").length();
                        for(int i=0; i<size; i++){
                            Course course = new Course();
                            course.setCourseName(response.getJSONArray("Courses").getString(i));
                            Log.e("JSON COURSE NAME", response.getJSONArray("Courses").getString(i));
                            course_list.add(course);
                        }
                        recomCourses(course_list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR RESPONSE", error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void recomCourses(List<Course> course_list) {
        recyclerView_courses.setAdapter(adapter);
        recyclerView_courses = findViewById(R.id.rv_courses);
        recyclerView_courses.setHasFixedSize(true);
        layoutManager_courses = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_courses.setLayoutManager(layoutManager);
        adapter_courses = new adapter_courses_recom(course_list, this);
        recyclerView_courses.setAdapter(adapter_courses);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        course_list = new ArrayList<>();
        Intent intent = getIntent();
        topic_id = intent.getStringExtra("Topic Id");
        course_id = intent.getStringExtra("Course Id");
        course_name = intent.getStringExtra("Course Name");
        fetchData(course_name);
        //fetchCourses(course_name);
        Log.e(Constants.TAG, "HELLOOOOOO FROM GURLEEN");
        Log.e(Constants.TAG, String.valueOf(topic_id==null)+String.valueOf(course_id==null));
        if(topic_id!=null && course_id!=null){
            Log.e(Constants.TAG, "Topic and Course Ids"+topic_id+" "+course_id);
        }

        ib = findViewById(R.id.ib_show_video_list);
        flag = 0;
        parent = findViewById(R.id.ll_root);
        parent.setVisibility(View.GONE);
        List<LessonInfo> lessonInfoList = new ArrayList<>();
        for (int j=0; j<5; j++){
            LessonInfo lessonInfo = new LessonInfo();
            lessonInfo.setLesson_no(j+1);
            lessonInfo.setLesson_name("Lesson "+j);
            lessonInfo.setLesson_desc("Lesson Description "+j);
            lessonInfoList.add(lessonInfo);
        }
        for(int j=0; j<5; j++){
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v;
            v = layoutInflater.inflate(R.layout.rv_video_list_card, null);
            parent.addView(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseActivity.this, VideoPlayerActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void showVideoList(View view) {
        if(flag==0){
            flag = 1;
            parent.setVisibility(View.VISIBLE);
            ib.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }
        else {
            flag = 0;
            parent.setVisibility(View.GONE);
            ib.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchData(String c_name) {
        JSONObject jsonObject = new JSONObject();
        //String url = "https://finalapi.herokuapp.com/api";
        String url = "https://linkedinapikavita.herokuapp.com/api";
        try {
            jsonObject.put("course_name", c_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "BEFORE REQUEST");
        if(isNetworkAvailable()){

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                    url,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("RESPONSE FROM SERVER", response.toString());
                    List<String> profiles = new ArrayList<>();
                    List<String> links = new ArrayList<>();
                    JSONArray jsonArray;
                    try {
                        jsonArray = response.getJSONArray("Profiles");
                        Log.e(Constants.TAG, "json array "+ jsonArray.toString() + jsonArray.length());
                        int size = jsonArray.length();
                        for (int i=0; i<size; i++){
                            profiles.add(jsonArray.getString(i));
                            Log.e(Constants.TAG, profiles.get(i));
                            links.add("https://www.linkedin.com/in/gurleen97kaur");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showData(profiles, links);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR RESPONSE", error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }

    }

    private void showData(List<String> profile_names, List<String> profile_links) {Log.e(
            Constants.TAG, String.valueOf(profile_names.size()));
        //com.android.volley.TimeoutError
        recyclerView = findViewById(R.id.rv_profile);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new adapter_rv_profile(profile_names, profile_links);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProfileSelected(String link) {

    }

/*
    private void openCourseActivity(DataSnapshot dataSnapshot, Course course) {
        for(int i=0; i<dataSnapshot.getChildrenCount(); i++){
            for (DataSnapshot t : dataSnapshot.child(String.valueOf(i)).getChildren()){
                Log.e(Constants.TAG+" OUT", String.valueOf(course.getCourseName()).toLowerCase());
                Log.e(Constants.TAG+" OUT", String.valueOf(t.child("CourseName").getValue()).toLowerCase());
                if(String.valueOf(t.child("CourseName").getValue()).toLowerCase().equals(String.valueOf(course.getCourseName()).toLowerCase())){
                    Log.e(Constants.TAG+" IN", String.valueOf(t.child("CourseName").getValue()).toLowerCase());
                    course.setCourseId(Integer.parseInt(t.getKey()));
                    course.setTopicId(i);
                }
            }
        }
        Log.e(Constants.TAG, "Topic and Course Ids"+course.getTopicId()+" "+course.getCourseId());
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra("Course Id", String.valueOf(course.getCourseId()));
        intent.putExtra("Topic Id", String.valueOf(course.getTopicId()));
        startActivity(intent);
    }*//*
    @Override
    public void onCourseSelected(final Course course) {

        Log.e(Constants.TAG, "onCourseSelected() called");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final DataSnapshot[] d = new DataSnapshot[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(Constants.TAG, "onDataChanged() called");
                d[0] = dataSnapshot;
                openCourseActivity(d[0], course);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
