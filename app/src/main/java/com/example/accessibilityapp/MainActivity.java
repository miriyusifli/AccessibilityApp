package com.example.accessibilityapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accessibilityapp.app.models.AppAdapter;
import com.example.accessibilityapp.utils.Utils;

public class MainActivity extends AppCompatActivity {


    private AppAdapter installedAppAdapter;
    public static String targetPackage;
    public static int iteration = 0;
    private static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView installedAppsList = findViewById(R.id.installed_app_list);
        builder();

        installedAppAdapter = new AppAdapter(MainActivity.this, Utils.getInstalledApps(this));
        installedAppsList.setAdapter(installedAppAdapter);
        installedAppsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                dialog.show();
                targetPackage = installedAppAdapter.getItem((int) l).getPackageName();

            }
        });

        String abc = installedAppsList.getCount() + "";
        TextView countApps = findViewById(R.id.countApps);
        countApps.setText(getResources().getString(R.string.total_installed_apps, abc));

    }


    private void builder(){


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(input)
                .setTitle("Iteration count")
                .setMessage("Please, insert iteration count.")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {


                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        iteration = Integer.valueOf(input.getText().toString());

                        if (iteration <= 0) {
                            Toast.makeText(getApplicationContext(), "Iteration count should be at least 1.", Toast.LENGTH_LONG).show();

                            return;

                        }
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

}