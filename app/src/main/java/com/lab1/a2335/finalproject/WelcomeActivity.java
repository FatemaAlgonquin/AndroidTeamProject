package com.lab1.a2335.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class WelcomeActivity extends  AppCompatActivity  {

    String TAG = "WelcomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = (Toolbar) findViewById(R.id.welcome_toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome_toolbar_menu, menu);

        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_one:
                Log.i("Toolbar", "Option 1 is selected");
                Intent intent = new Intent(this, MainActivityTracker.class);
                this.startActivity(intent);
                break;

            case R.id.action_two:
                Log.i("Toolbar", "Option 2 selected");
                Toast.makeText(this.getApplicationContext(), "Joanne is still working on her project!",
                Toast.LENGTH_LONG).show();

                break;

            case R.id.action_three:
                Log.i("Toolbar", "Option 3 selected");
                Toast.makeText(this.getApplicationContext(), "Geeta is still working on her project!",
                        Toast.LENGTH_LONG).show();
                break;


            case R.id.action_settings:
                Snackbar.make(findViewById(R.id.welcome_toolbar),"Group project by Fatema, Joanne, Geeta", Snackbar.LENGTH_LONG).show();
            default:
                break;
        }
        return true;
    }


}
