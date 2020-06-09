package com.example.a3dprintertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //Setting up printer ArrayList
    public static ArrayList<Printer> printerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up local variables for linear layout and add printer button
         LinearLayout printerListScreen = findViewById(R.id.printerListLinLayout);
        Button add_Printer = findViewById(R.id.addPrinterBtn);

        //Sets up for calling openAddPrinterScreen() when clicking on add printer button
        add_Printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPrinterScreen();
            }
        });

        //Loads printer list first, then saves it
         loadPrinterList();
        savePrinterList();

        //If printers exist in the list, this creates a button for all of them, sizes them correctly
        //and sets them to call openSpecificPrinterHomeScreen when clicked
         if (!printerList.isEmpty())
        {
            for (int i = 0; i < printerList.size(); i++) {

                Button btn = new Button(MainActivity.this);
                btn.setId(i);
                btn.setWidth(40);
                btn.setHeight(20);
                btn.setText(printerList.get(i).getName());
                printerListScreen.addView(btn);
                Button btnAdded = findViewById(i);
                btnAdded.getLayoutParams().width=1050;
                btnAdded.setSingleLine(true);
                btnAdded.setEllipsize(TextUtils.TruncateAt.END);
                btnAdded.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        openSpecificPrinterHomeScreen(view.getId());
                    }
                });
            }
        }

    }

    // function to open Add Printer Screen
    public void openAddPrinterScreen()
    {
        Intent openAddPrinterScreen = new Intent(this, AddPrinterScreen.class);
        startActivity(openAddPrinterScreen);
    }

    //Function to load the printer list from the Shared Preferences if the list is not currently loaded
    private void loadPrinterList() {
        if (printerList == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("printer list", null);
            Type type = new TypeToken<ArrayList<Printer>>() {}.getType();
            printerList = gson.fromJson(json, type);

            if (printerList == null) {
                printerList = new ArrayList<>();
            }
        }
    }

    //Function to save the printer list the the Shared Preferences
    public void savePrinterList()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(printerList);
        editor.putString("printer list", json);
        editor.apply();
    }

    //Function to open the SpecificPrinterHomeScreen Activity
    public void openSpecificPrinterHomeScreen(int printer_Number)
    {
        Intent openSpecificPrinterHomeScreen = new Intent(this, specificPrinterHomeScreen.class);
        openSpecificPrinterHomeScreen.putExtra("Printer Number", String.valueOf(printer_Number));
        startActivity(openSpecificPrinterHomeScreen);
    }
    }

