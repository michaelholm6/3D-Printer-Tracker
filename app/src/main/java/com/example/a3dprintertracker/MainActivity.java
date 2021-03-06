package com.example.a3dprintertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.calendar.model.Setting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements confirmGoogleSignOut.confirmGoogleSignOutListener {

    //Setting up Main Activity variables
    public static ArrayList<Printer> printerList;
    public static Button signOutUser;
    private GoogleSignInClient mGoogleSignInClient;
    int pauseRefresh, restartActivity;
    MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tracking if the screen needs to refreshed
         mainActivity = this;
        restartActivity = 0;

        //Setting up Google Sign in use
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Setting up Sign Out User button
        signOutUser = findViewById(R.id.signOutButton);
        signOutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

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

        //Handles visibility of the signOutUser button and the online notification
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (GoogleSignIn.getLastSignedInAccount(MainActivity.this) != null)
                {
                    signOutUser.setVisibility(View.VISIBLE);
                }
                else
                {
                    signOutUser.setVisibility(View.INVISIBLE);
                }
                if (!isOnline() && mainActivity.hasWindowFocus())
                {
                    showConnectionDialog();
                }
                else if (isOnline() && restartActivity == 1)
                {
                    restartActivity = 0;
                    pauseRefresh = 0;
                    //Intent intent = getIntent();
                    recreate();
                }
                handler.postDelayed(this, 100);
            }
        });

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

    //Function to open dialog box when signing out of Google
    public void openDialog()
    {
        confirmGoogleSignOut ConfirmGoogleSignOut = new confirmGoogleSignOut();
        ConfirmGoogleSignOut.show(getSupportFragmentManager(), "confirm google sign out");
    }

    //Function to sign out of Google when Yes is clicked
    @Override
    public void onYesClicked() {
       mGoogleSignInClient.revokeAccess();
    }

    //Function to determine whether the user is online or not
    public boolean isOnline()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    //Function to bring up the connection status dialog box and manage refreshing the screen
    private void showConnectionDialog()
    {
        pauseRefresh = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires an internet connection to function, please either connect or close the app.")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNeutralButton("Connect to Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
                    }
                })
                .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        restartActivity = 1;
    }
    }

