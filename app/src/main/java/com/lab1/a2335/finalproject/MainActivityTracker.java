package com.lab1.a2335.finalproject;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivityTracker extends ToolbarActivity {
    Button runningBtn;
    Button walkingBtn;
    Button bikingBtn;
    DatePicker datePicker;

    String TAG = "MainActivityTracker";
    String sendBackMsgToOne = "Option 1 is selected";
    ExerciseDatabaseHelper exerciseDatabaseHelper;
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tracker);

        exerciseDatabaseHelper = new ExerciseDatabaseHelper(getApplicationContext());
        exerciseDatabaseHelper.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datePicker = (DatePicker)findViewById(R.id.datePicker2);
        final Calendar currentCalendar = Calendar.getInstance();
        final Calendar selectedCalendar = Calendar.getInstance();

        datePicker.init(currentCalendar.get(Calendar.YEAR),currentCalendar.get(Calendar.MONTH),currentCalendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        selectedCalendar.set(Calendar.YEAR, year);
                        selectedCalendar.set(Calendar.MONTH, monthOfYear);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        showSummary(selectedCalendar);
                    }
                });



        runningBtn = (Button)findViewById(R.id.buttonRunning);

        runningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "parentActivity buttons is clicked");
                Intent intent=new Intent(view.getContext(), DailyList.class);

                Date date = selectedCalendar.getTime();
                String selectedDate = dateformat.format(date);


                intent.putExtra("SELECTED_DATE",selectedDate);
                //Intent intent=new Intent(view.getContext(), runningActivity.class);
                startActivity(intent);
            }
        });

        showSummary(selectedCalendar);

       // FrameLayout frameLayout = (FrameLayout)findViewById(R.id.entryTypeSummary);

    }


    /**
     * Converts the selected date as string to be used in the database query. Note that there is no date data type in postgres.
     * @param selectedCalendar selected date in the calendar.
     * @return string representation of the selected date.
     */
    private String getSelectedDateAsString(Calendar selectedCalendar){
        Date date = selectedCalendar.getTime();
        String selectedDate = dateformat.format(date);

        return selectedDate;
    }

    /**
     * Display the summary section
     * @param selectedCalendar
     */
    private void showSummary(Calendar selectedCalendar){

        String dateAsString = getSelectedDateAsString(selectedCalendar);
        MonthlySummary fragment = new MonthlySummary();
        //fragment.setParentActivity(this);
        Bundle bundle = new Bundle();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM, yyyy");
        String yearMonth = dateFormatter.format(selectedCalendar.getTime());

        bundle.putString("summaryValue", yearMonth);

        int totalTime = exerciseDatabaseHelper.getTotalActivityByMonth(dateAsString);

        int totalDaysInMonth = selectedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        double monthlyAvg = totalTime/totalDaysInMonth;
        DecimalFormat df = new DecimalFormat("#0.00");

        bundle.putString("avgRunning",df.format(monthlyAvg));
        //bundle.putLong("dbId",id);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.entryTypeSummary, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
