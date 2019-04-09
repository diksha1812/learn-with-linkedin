package com.example.extra.myapplication.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;

import java.util.List;

/**
 * Created by Gurleen on 17-01-2019.
 */
public class adapter_rv_keywords extends RecyclerView.Adapter<adapter_rv_keywords.ViewHolder> {
    private List<String> keywords_list;
    private adapter_rv_keywords.adapter_rv_keywords_listener listener;

    public adapter_rv_keywords (List<String> keywords_list, adapter_rv_keywords.adapter_rv_keywords_listener listener){
        this.keywords_list = keywords_list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_keyword;
        public ViewHolder(View v) {
            super(v);
            tv_keyword = v.findViewById(R.id.tv_keyword);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onKeywordSelected(keywords_list.get(getAdapterPosition()));
                }
            });
        }
    }
    @NonNull
    @Override
    public adapter_rv_keywords.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_keywords_layout, viewGroup, false);
        adapter_rv_keywords.ViewHolder vh = new adapter_rv_keywords.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rv_keywords.ViewHolder viewHolder, int i) {
        viewHolder.tv_keyword.setText(keywords_list.get(i));
    }

    @Override
    public int getItemCount() {
        if(keywords_list!=null)
            return keywords_list.size();
        else return 0;
    }

    public interface adapter_rv_keywords_listener{
        void onKeywordSelected(String keyword);
    }
}
