package com.lab1.a2335.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MessageDetails extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String commentMsg = getIntent().getExtras().getString("commentMsg");
        int id = getIntent().getExtras().getInt("Id");
        String duration = getIntent().getExtras().getString("durationMsg");
        String dateStr = getIntent().getExtras().getString("dateMsg");

        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("commentMsg", commentMsg);
        bundle.putString("durationMsg", duration);
        bundle.putString("dateMsg", dateStr);
        bundle.putInt("Id",id);

        //bundle.putLong("dbId",id);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        //ft.replace(R.id.entryType, fragment);
        ft.commit();

    }
}
