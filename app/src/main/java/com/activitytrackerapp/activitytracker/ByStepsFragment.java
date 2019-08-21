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

public class ByStepsFragment extends Fragment {
    
    public static final String TAG = "ByStepFragment";

    public RecyclerView stepsRecyclerView;
    public StepsListAdapter stepsAdapter;
    public RecyclerView.LayoutManager stepsLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_by_steps, container, false);

        stepsRecyclerView = view.findViewById(R.id.recyclerViewSteps);
        stepsRecyclerView.setHasFixedSize(true);
        stepsLayoutManager = new LinearLayoutManager(getContext());
        stepsAdapter = new StepsListAdapter(DataHandler.steps);

        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
        stepsRecyclerView.setAdapter(stepsAdapter);

        DataHandler.setStepsListAdapter(stepsAdapter);
        DataHandler.setStepsLayoutManager(stepsLayoutManager);
        stepsAdapter.notifyDataSetChanged();

        stepsLayoutManager.scrollToPosition(DataHandler.steps.size()-1);

        return view;
    }

}
