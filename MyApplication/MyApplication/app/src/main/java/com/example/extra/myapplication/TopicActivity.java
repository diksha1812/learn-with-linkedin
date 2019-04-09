package com.example.extra.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.example.extra.myapplication.Adapters.adapter_rv_search;
import com.example.extra.myapplication.DataClasses.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity implements adapter_rv_search.adapter_rv_search_listener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    List<Course> course_list;
    SearchView searchView;
    String topic_id;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        //searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((adapter_rv_search) adapter).getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                ((adapter_rv_search) adapter).getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        topic_id = getIntent().getStringExtra("Topic Id");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(this);
        course_list = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_topicwise_courses);
        recyclerView.setLayoutManager(layoutManager);

        fetchData();
    }

    private void fetchData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final DataSnapshot[] d = new DataSnapshot[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(Constants.TAG, "onDataChanged() called");
                d[0] = dataSnapshot;
                showData(d[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        DataSnapshot x = dataSnapshot.child(topic_id);
        toolbar.setTitle(String.valueOf(x.child("TopicName").getValue()));
        for (DataSnapshot temp : x.getChildren()){
            if((temp.child("CourseName").getValue())!=null){
                Course course = new Course();
                course.setCourseName(String.valueOf(temp.child("CourseName").getValue()));
                Log.e(Constants.TAG, String.valueOf(temp.child("CourseName").getValue()));
                course.setCourseDescription(String.valueOf(temp.child("CourseDesc").getValue()));
                course_list.add(course);
            }
        }

        adapter = new adapter_rv_search(TopicActivity.this, course_list,TopicActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCourseSelected(Course course) {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }
}
