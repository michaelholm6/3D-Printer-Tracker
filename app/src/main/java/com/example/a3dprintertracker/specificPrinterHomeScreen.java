package com.example.a3dprintertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class specificPrinterHomeScreen extends AppCompatActivity implements confirmDeletePrinter.confirmPrinterDeleteListener {

    //Sets up integer to receive from previous activity
    int printer_Number_Int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_printer_home_screen);

        //Receives printer number from previous activity
        final String printer_Number = getIntent().getStringExtra("Printer Number");

        //Sets up button for printer details
        Button printerDetails = findViewById(R.id.printerDetails);
        printerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdatePrinterDetails(printer_Number_Int);
            }
        });

        //Converts the received string to an int
        printer_Number_Int = Integer.valueOf(printer_Number);

        //Sets up the delete printer button, removes it from the printer list and opens the
        //main activity
        Button deletePrinter = findViewById(R.id.printerDelete);
        deletePrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });
    }

    //Function to open the main activity
     private void openMainActivity()
    {
        Intent openMainActivity = new Intent(this, MainActivity.class);
        startActivity(openMainActivity);
    }

    //Function to open UpdatePrinterDetails screen while passing printer number
    private void openUpdatePrinterDetails(int printer_Number)
    {
        Intent openUpdatePrinterDetails = new Intent(this, updatePrinterDetails.class);
        openUpdatePrinterDetails.putExtra("Printer Number", String.valueOf(printer_Number));
        startActivity(openUpdatePrinterDetails);
    }

    //Function to open the dialog to confirm the deletion of a printer
    public void openDialog()
    {
        confirmDeletePrinter ConfirmPrinterDelete = new confirmDeletePrinter();
        ConfirmPrinterDelete.show(getSupportFragmentManager(), "confirm printer delete");
    }

    //Function describing what to do if yes is clicked on the dialog
     @Override
    public void onYesClicked() {
        MainActivity.printerList.remove(printer_Number_Int);
        openMainActivity();
    }
}
