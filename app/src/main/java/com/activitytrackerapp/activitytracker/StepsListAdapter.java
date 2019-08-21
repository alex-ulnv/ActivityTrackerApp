package com.activitytrackerapp.activitytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListViewHolder> {

    private ArrayList<Step> steps;

    public static class StepsListViewHolder extends RecyclerView.ViewHolder {

        public TextView step_num, type, timestamp;

        public StepsListViewHolder(@NonNull View itemView) {
            super(itemView);
            step_num = itemView.findViewById(R.id.step_item_number);
            type = itemView.findViewById(R.id.step_item_type);
            timestamp = itemView.findViewById(R.id.step_item_timestamp);
        }
    }

    public StepsListAdapter(ArrayList<Step> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new StepsListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListViewHolder holder, int position) {

        Step step = steps.get(position);
        StepItem stepItem = new StepItem(step, position);
        holder.step_num.setText(stepItem.getStep_num());
        holder.type.setText(stepItem.getType());
        holder.timestamp.setText(stepItem.getTimestamp());

        // step at index 0 is a system step that is used for reference only - don't show it
        if (stepItem.getStep_num().equals("0")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    // a wrapper method to ensure the method is "mockable" during testing
    public void localNotifyItemInserted(int position) {
        this.notifyItemInserted(position);
    }
}
