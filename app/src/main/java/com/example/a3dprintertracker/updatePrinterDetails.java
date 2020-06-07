package com.example.a3dprintertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class updatePrinterDetails extends AppCompatActivity {
    //Sets up all class variables
    private EditText printer_Name, printer_Serial, printer_Manufacturer, amount_Paid;
    private Button purchaseDate;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_printer_details);

        //Retrieves information passed from the previous activity
        final String printer_Number = getIntent().getStringExtra("Printer Number");

        //Sets up all the buttons on this screen
        printer_Name = findViewById(R.id.PrinterName);
        printer_Serial = findViewById(R.id.printerSerialNumber);
        printer_Manufacturer = findViewById(R.id.printerManufacturer);
        amount_Paid = findViewById(R.id.amountPaid);

        //Retrieves printer info and populates the text fields
        printer_Name.setText(MainActivity.printerList.get(Integer.valueOf(printer_Number)).getName());
        printer_Serial.setText(MainActivity.printerList.get(Integer.valueOf(printer_Number)).getSerial());
        printer_Manufacturer.setText(MainActivity.printerList.get(Integer.valueOf(printer_Number)).getManufacturer());
        amount_Paid.setText(MainActivity.printerList.get(Integer.valueOf(printer_Number)).getAmount_Paid());

        //Sets up the Purchase Date button with the calendar
        purchaseDate = findViewById(R.id.purchaseDate);
        purchaseDate.setText(MainActivity.printerList.get(Integer.valueOf(printer_Number)).getDate_Purchased());
        purchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                setListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        purchaseDate.setText(date);
                    }
                };
                closeKeyboard();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        updatePrinterDetails.this, android.R.style.Theme_DeviceDefault_Dialog, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //Sets up the Update Printer Button
        Button updatePrinter = findViewById(R.id.addPrinterBtn);
        updatePrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!printer_Name.getText().toString().matches(""))
                {
                    Printer newPrinter = new Printer(printer_Name.getText().toString(), printer_Serial.getText().toString(),
                            printer_Manufacturer.getText().toString(), amount_Paid.getText().toString(), purchaseDate.getText().toString());
                    MainActivity.printerList.set(Integer.valueOf(printer_Number), newPrinter);
                }
                openMainActivity();
            }
        });
    }

    //Function to open the Main Activity
      public void openMainActivity()
    {
        Intent openMainActivity = new Intent(this, MainActivity.class);
        startActivity(openMainActivity);
    }

    //Function to close the keyboard when opening the calendar dialog
     public void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}


