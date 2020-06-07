package com.example.a3dprintertracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class confirmDeletePrinter extends AppCompatDialogFragment {
    private confirmPrinterDeleteListener listener;
    @Override

    //Function for creating the confirm printer deletion dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Printer Deletion")
                .setMessage("Are you sure you wish to delete this printer and all of its data?")
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
    public interface confirmPrinterDeleteListener
    {
        void onYesClicked();
    }

    //Attaches functions to the confirm printer delete dialog
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (confirmPrinterDeleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement confirmPrinterDeleteListener");
        }


    }
}
