package com.lab1.a2335.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

   

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

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
                Toast toast = Toast.makeText(this.getApplicationContext(), "Following steps will guide you to get help!" +
                        "1: Summary page: clicking a data picker will display list of activities\n" +
                        "2: Daily activity list: clicking an activity display detailed information on the " +
                        "right panel.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT,0,0);
                toast.show();
                break;
            case R.id.action_two:
                Log.i("Toolbar", "Option 2 selected");
                // AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplication());
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                builder.setTitle(R.string.pick_color);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_three:
                Intent intent = new Intent(this, WeatherForecast.class);
                this.startActivity(intent);
                break;


            case R.id.action_settings:
                Snackbar.make(findViewById(R.id.toolbar),"Version: 1.0, Developed by: Fatema Zohora!", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(this.getApplicationContext(), "Version: 1.0 \n Developed by: Fatema Zohora!",
                        //Toast.LENGTH_LONG).show();
                // 

            default:
                break;
        }
        return true;
    }

}
