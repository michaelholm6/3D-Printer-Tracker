package com.example.a3dprintertracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class confirmGoogleSignOut extends AppCompatDialogFragment {
    private confirmGoogleSignOutListener listener;
    @Override

    //Function for creating the confirm Google Sign Out dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Google Sign Out")
                .setMessage("Are you sure you wish to disconnect Google and all of its related data?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    listener.onYesClicked();
                    }
                });

        return builder.create();
    }

    //Waits for the yes button to be clicked on the dialog box
    public interface confirmGoogleSignOutListener
    {
        void onYesClicked();
    }

    //Attaches functions to the confirm Google Sign Out dialog
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (confirmGoogleSignOutListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement confirmPrinterDeleteListener");
        }


    }
}
