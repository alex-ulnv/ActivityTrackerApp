package com.activitytrackerapp.activitytracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class StartUpDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.first_start_notification_title)
                .setMessage(R.string.first_start_notification_message)
                .setPositiveButton(R.string.first_start_notification_train_bttn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new TrainFragment())
                                .commit();
                    }
                })
                .setNegativeButton(R.string.first_start_notification_accept_default, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity)getActivity()).setDefaultThresholds();
                    }
                });
        return builder.create();
    }
}
