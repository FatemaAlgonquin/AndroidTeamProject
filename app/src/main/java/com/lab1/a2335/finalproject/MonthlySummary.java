package com.lab1.a2335.finalproject;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fatema on 2017-12-24.
 */

public class MonthlySummary extends Fragment {

    String summaryValue;
    String  avgRunning;
    TextView lblSummary;
    TextView summaryContent;
    TextView avgLabel;
    TextView avgContent;
    TextView idView;

    public MonthlySummary() {
        // Required empty public constructor
    }

    public static MonthlySummary newInstance()
    {
        MonthlySummary myFragment = new MonthlySummary();

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            summaryValue = bundle.getString("summaryValue");
            avgRunning = bundle.getString("avgRunning");
            //dbID = bundle.getLong("dbId");
            Log.i("MonthlySummary", summaryValue);
        }
        Log.i("Monthly Summary", "I am in MonthlySummary fragment class");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_summary, container, false);
       // lblSummary = (TextView) view.findViewById(R.id.summaryLabel);
        //lblSummary.setTextSize(30);

        summaryContent = (TextView)view.findViewById(R.id.summaryDate);
        summaryContent.setText(summaryValue);
        //summaryContent.setTextSize(30);

        //avgLabel = (TextView) view.findViewById(R.id.avgRunningLabel);
        //avgLabel.setTextSize(30);


        avgContent = (TextView)view.findViewById(R.id.avgRunnig);
        avgContent.setText(avgRunning);
        //avgContent.setTextSize(30);

        return view;
    }
}
