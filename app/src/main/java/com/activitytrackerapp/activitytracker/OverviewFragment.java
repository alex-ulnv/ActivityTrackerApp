package com.activitytrackerapp.activitytracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OverviewFragment extends Fragment {

    public RecyclerView activitiesRecyclerView;
    public ActivitiesListAdapter activitiesAdapter;
    public RecyclerView.LayoutManager activitiesLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        activitiesRecyclerView = view.findViewById(R.id.recyclerViewActivities);
        activitiesRecyclerView.setHasFixedSize(true);
        activitiesLayoutManager = new LinearLayoutManager(getContext());
        activitiesAdapter = new ActivitiesListAdapter(DataHandler.activities);

        activitiesRecyclerView.setLayoutManager(activitiesLayoutManager);
        activitiesRecyclerView.setAdapter(activitiesAdapter);

        DataHandler.setActivitiesListAdapter(activitiesAdapter);
        DataHandler.setActivitiesLayoutManager(activitiesLayoutManager);
        activitiesAdapter.notifyDataSetChanged();

        activitiesLayoutManager.scrollToPosition(DataHandler.activities.size()-1);

        return view;
    }
}
