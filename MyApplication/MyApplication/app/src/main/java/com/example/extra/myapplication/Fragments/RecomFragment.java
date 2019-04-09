package com.example.extra.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.extra.myapplication.Adapters.adapter_rv_recom;
import com.example.extra.myapplication.Adapters.adapter_rv_keywords;
import com.example.extra.myapplication.Constants;
import com.example.extra.myapplication.CourseActivity;
import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.MainActivity;
import com.example.extra.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecomFragment extends Fragment implements adapter_rv_recom.adapter_rv_recom_listener, adapter_rv_keywords.adapter_rv_keywords_listener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView rv, rv_keywords;
    private RecyclerView.LayoutManager layoutManager, layoutManager_keywords;
    private RecyclerView.Adapter adapter, adapter_keywords;
    private LinearLayout ll_keywords, ll_recom;
    private Button bt_show_keywords;

    public RecomFragment() {
        // Required empty public constructor
    }

    public static RecomFragment newInstance(String param1, String param2) {
        RecomFragment fragment = new RecomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recom, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Recommendations");
        ll_keywords = view.findViewById(R.id.ll_keywords);
        ll_recom = view.findViewById(R.id.ll_recom);
        rv_keywords = view.findViewById(R.id.rv_keywords);
        layoutManager_keywords = new GridLayoutManager(getContext(), 3);

        rv = view.findViewById(R.id.rv_recom);
        layoutManager = new GridLayoutManager(getContext(), 2);
        bt_show_keywords = view.findViewById(R.id.bt_show_keywords);
        bt_show_keywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_recom.setVisibility(View.GONE);
                ll_keywords.setVisibility(View.VISIBLE);
            }
        });
        //fetchData();
        showKeywords();
        return view;
    }


    private void showKeywords() {
        rv_keywords.setLayoutManager(layoutManager_keywords);
        adapter_keywords = new adapter_rv_keywords(Constants.keywords, RecomFragment.this);
        rv_keywords.setAdapter(adapter_keywords);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchData(String keyword) {
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
                        showData(course_list);
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

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void showData(List<Course> course_list) {
        rv.setLayoutManager(layoutManager);
        adapter = new adapter_rv_recom(course_list, RecomFragment.this);
        rv.setAdapter(adapter);
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
    }

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
        Intent intent = new Intent(getContext(), CourseActivity.class);
        intent.putExtra("Course Id", String.valueOf(course.getCourseId()));
        intent.putExtra("Topic Id", String.valueOf(course.getTopicId()));
        startActivity(intent);
    }

    @Override
    public void onKeywordSelected(String keyword) {
        fetchData(keyword);
        ll_keywords.setVisibility(View.GONE);
        ll_recom.setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
