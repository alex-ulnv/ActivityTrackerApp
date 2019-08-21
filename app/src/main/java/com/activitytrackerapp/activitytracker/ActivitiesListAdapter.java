package com.activitytrackerapp.activitytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivitiesListAdapter extends RecyclerView.Adapter<ActivitiesListAdapter.ActivitiesListViewHolder> {

    private ArrayList<UserActivity> activities;

    public static class ActivitiesListViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView title, dates_from, dates_to, steps;

        public ActivitiesListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.activity_item_image);
            title = itemView.findViewById(R.id.activity_item_title);
            dates_from = itemView.findViewById(R.id.activity_item_dates_from);
            dates_to = itemView.findViewById(R.id.activity_item_dates_to);
            steps = itemView.findViewById(R.id.activity_item_steps);
        }
    }

    public ActivitiesListAdapter(ArrayList<UserActivity> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivitiesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        ActivitiesListViewHolder alvh = new ActivitiesListViewHolder(v);
        return alvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesListViewHolder holder, int position) {

        UserActivity userActivity = activities.get(position);
        ActivityItem activityItem = new ActivityItem(userActivity);

        holder.imageView.setImageResource(activityItem.getImageResource());
        holder.title.setText(activityItem.getTitle());
        holder.dates_from.setText(activityItem.getDatesFrom());
        holder.dates_to.setText(activityItem.getDatesTo());
        holder.steps.setText("Steps: " + activityItem.getSteps());

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    // a wrapper methods to ensure the method is "mockable" during testing
    public void localNotifyItemInserted(int position) {
        this.notifyItemInserted(position);
    }

    public void localNotifyItemChanged(int position) {
        this.notifyItemChanged(position);
    }
}
