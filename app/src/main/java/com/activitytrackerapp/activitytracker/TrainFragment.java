package com.activitytrackerapp.activitytracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrainFragment extends Fragment implements View.OnClickListener {

    Trainer trainer;
    Button startBttn, updateThresholdsBttn;
    TextView resultsTextView;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate
        View view = inflater.inflate(R.layout.fragment_train, container, false);

        // find the views
        startBttn = view.findViewById(R.id.trainStartBttn);
        updateThresholdsBttn = view.findViewById(R.id.updateThresholdBttn);
        resultsTextView = view.findViewById(R.id.trainResultsTextView);
        progressBar = view.findViewById(R.id.trainProgressBar);

        // add onClick listeners to clickable views
        startBttn.setOnClickListener(this);
        updateThresholdsBttn.setOnClickListener(this);

        // instantiate a Trainer object
        trainer = new Trainer(getActivity());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.trainStartBttn:
                trainer.train();
                break;
            case R.id.updateThresholdBttn:
                trainer.updateThresholds();
                resetFragment();
                break;
        }
    }

    public void resetFragment() {
        startBttn.setText(R.string.start_button);
        startBttn.setEnabled(true);
        updateThresholdsBttn.setVisibility(View.GONE);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.GONE);
        resultsTextView.setText("");
        resultsTextView.setVisibility(View.GONE);
    }
}
