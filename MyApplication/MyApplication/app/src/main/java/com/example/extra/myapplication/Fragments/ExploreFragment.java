package com.example.extra.myapplication.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.extra.myapplication.Adapters.adapter_rv_horizontal;
import com.example.extra.myapplication.Adapters.adapter_rv_search;
import com.example.extra.myapplication.Adapters.adapter_rv_vertical;
import com.example.extra.myapplication.Constants;
import com.example.extra.myapplication.CourseActivity;
import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;
import com.example.extra.myapplication.TopicActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExploreFragment extends Fragment implements adapter_rv_search.adapter_rv_search_listener,
        adapter_rv_horizontal.adapter_rv_h_listener {
    RecyclerView rv_vertical, rv_search;
    RecyclerView.LayoutManager layoutManager, layoutManager_search;
    RecyclerView.Adapter adapter, adapter_search;
    LinearLayout ll_search;
    SearchView searchView;
    List<Course> course_list;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    DataSnapshot dataSnapshot;

    private OnFragmentInteractionListener mListener;

    List<Course> courses;

    public ExploreFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Log.e(Constants.TAG, String.valueOf(courses.size()));
                if (query.isEmpty()){
                    rv_vertical.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.GONE);
                }
                else{
                    rv_vertical.setVisibility(View.GONE);
                    ll_search.setVisibility(View.VISIBLE);
                    ((adapter_rv_search) adapter_search).getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                //Log.e(Constants.TAG, String.valueOf(courses.size()));
                if (query.isEmpty()){
                    rv_vertical.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.GONE);
                }
                else{
                    rv_vertical.setVisibility(View.GONE);
                    ll_search.setVisibility(View.VISIBLE);
                    ((adapter_rv_search) adapter_search).getFilter().filter(query);
                }
                return false;
            }
        });
    //    return true;
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        rv_vertical = view.findViewById(R.id.rv_vertical);
        ll_search = view.findViewById(R.id.ll_search);
        layoutManager_search = new LinearLayoutManager(getContext());
        rv_search = view.findViewById(R.id.rv_search_results);
        fetchData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

        @Override
        public void onCourseSelected(Course course) {
            //Toast.makeText(this, "Course Selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), CourseActivity.class);
            startActivity(intent);
        }

        @Override
        public void onCourseSelectedRVH(Course course, int flag, int topic_id) {
            //flag=0: TopicActivity
            //flag=1: CourseActivity

            //Toast.makeText(this, "Course Selected", Toast.LENGTH_SHORT).show();
            if(flag==0){
                Log.e(Constants.TAG, "Flag is 0");
                Intent intent = new Intent(getContext(), TopicActivity.class);
                intent.putExtra("Topic Id", String.valueOf(topic_id));
                intent.putExtra("Course Id", course.getCourseId());
                startActivity(intent);
            }
            else {
                Log.e(Constants.TAG, "Flag is 1 "+course.getCourseName());
                Intent intent = new Intent(getContext(), CourseActivity.class);
                //intent.putExtra("Course Id", course.getCourseId());
                intent.putExtra("Topic Id", String.valueOf(topic_id));
                intent.putExtra("Course Name", course.getCourseName());
                startActivity(intent);
            }
        }

    public void fetchData(){
        Log.e("log ", "Hi");
        //final List<Course> courses1 = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        final DataSnapshot[] d = new DataSnapshot[1];
        // Read from the database

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
        //return d[0];
    }

    private void showData(DataSnapshot dataSnapshot) {
        rv_vertical.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv_vertical.setLayoutManager(layoutManager);
        adapter = new adapter_rv_vertical(dataSnapshot, ExploreFragment.this);
        rv_vertical.setAdapter(adapter);
        course_list = new ArrayList<>();
        int k = 0;
        for(int i=0; i<dataSnapshot.getChildrenCount(); i++){
            for (DataSnapshot t : dataSnapshot.child(String.valueOf(i)).getChildren()){
                if(t.child("CourseName").getValue()!=null){

                    Course c = new Course();

                    c.setCourseId(Integer.parseInt(t.getKey()));
                    c.setCourseName(t.child("CourseName").getValue().toString());
                    c.setCourseDescription(t.child("CourseDesc").getValue().toString());
                    Log.e(Constants.TAG, "!!!!"+t.child("CourseName").getValue().toString());
                    Log.e(Constants.TAG, String.valueOf(t.getKey()));
                    course_list.add(c);
                }
            }
        }
        Log.e(Constants.TAG, "is Search Course list null? "+String.valueOf(course_list==null));
        rv_search.setLayoutManager(layoutManager_search);
        adapter_search = new adapter_rv_search(getContext(), course_list, ExploreFragment.this);
        rv_search.setAdapter(adapter_search);
    }
}
