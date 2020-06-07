package com.example.a3dprintertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.TextView;

import java.util.Calendar;

public class AddPrinterScreen extends AppCompatActivity {
    //Setting up buttons and Edit Texts on the Add Printer Screen
    Button addPrinter, purchaseDate;
    EditText printer_Name,printer_Serial, printer_Manufacturer, amount_Paid;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_printer_screen);

        //setting up calendar for the pick date button


        //relating all buttons to buttons on the XML file
         printer_Name = findViewById(R.id.PrinterName);
        printer_Serial = findViewById(R.id.printerSerialNumber);
        printer_Manufacturer = findViewById(R.id.printerManufacturer);
        amount_Paid = findViewById(R.id.amountPaid);

        //Brings up the calendar when selecting the purchase date button
        purchaseDate = findViewById(R.id.purchaseDate);
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
                        AddPrinterScreen.this, android.R.style.Theme_DeviceDefault_Dialog, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //Sets up the add printer button, adds the printer details to the printerList ArrayList under the main activity
        //unless printer name section is not filled out
        addPrinter = findViewById(R.id.addPrinterBtn);
        addPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!printer_Name.getText().toString().matches(""))
                {
                    Printer newPrinter = new Printer(printer_Name.getText().toString(), printer_Serial.getText().toString(),
                            printer_Manufacturer.getText().toString(), amount_Paid.getText().toString(), purchaseDate.getText().toString());
                    MainActivity.printerList.add(newPrinter);
                }
            openMainActivity();
            }
        });
    }

    //function for opening the Main Activity Screen
    public void openMainActivity()
{
    Intent openMainActivity = new Intent(this, MainActivity.class);
    startActivity(openMainActivity);
}

    //function for closing the soft keyboards when clicking on the choose date button
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