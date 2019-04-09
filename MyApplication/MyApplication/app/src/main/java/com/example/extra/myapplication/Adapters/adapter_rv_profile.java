package com.example.extra.myapplication.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.extra.myapplication.Constants;
import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;

import java.util.List;

/**
 * Created by Gurleen on 19-01-2019.
 */
public class adapter_rv_profile extends RecyclerView.Adapter<adapter_rv_profile.ViewHolder> {

    List<String> profile_name, profile_link;
    adapter_rv_profile.adapter_rv_profile_listener listener;

    public adapter_rv_profile(List<String> profile_name, List<String> profile_link){
        this.profile_link  = profile_link;
        this.profile_name = profile_name;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public ViewHolder(View v) {
            super(v);
            tv_name = v.findViewById(R.id.tv);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onProfileSelected(profile_link.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public adapter_rv_profile.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_profile_layout, viewGroup, false);
        adapter_rv_profile.ViewHolder vh = new adapter_rv_profile.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rv_profile.ViewHolder viewHolder, int position) {
        viewHolder.tv_name.setText(profile_name.get(position));
    }

    @Override
    public int getItemCount() {
        Log.e(Constants.TAG, "getItemCount "+String.valueOf(profile_name.size()));
        return profile_name.size();
    }

    public interface adapter_rv_profile_listener{
        void onProfileSelected(String link);
    }
}

