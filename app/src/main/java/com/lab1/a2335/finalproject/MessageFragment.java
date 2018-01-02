package com.lab1.a2335.finalproject;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    ImageButton deleteBtn;
    String commentValue;
    int myId;
    String duration;
    String exerciseDate;
    DailyList parentActivity;
    MessageFragment thisFragment;

    LayoutInflater inflater;
    ViewGroup viewGroup;

    public MessageFragment() {
        // Required empty public constructor
    }

    public void setParentActivity(DailyList parentActivity) {
        this.parentActivity = parentActivity;
    }

    public static MessageFragment newInstance() {
        MessageFragment myFragment = new MessageFragment();

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            commentValue = bundle.getString("commentMsg");
            myId = bundle.getInt("Id");
            duration = bundle.getString("durationMsg");
            exerciseDate = bundle.getString("dateMsg");

            //dbID = bundle.getLong("dbId");
            Log.i("MessageFragment", commentValue);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        thisFragment = this;
        this.inflater = inflater;
        this.viewGroup = container;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);

        TextView commentField = (TextView) view.findViewById(R.id.messageView);
        commentField.setText(commentValue);

        TextView durationField = (TextView) view.findViewById(R.id.durationId);
        durationField.setText(duration);

        TextView dateField = (TextView) view.findViewById(R.id.dateField);
        dateField.setText(exerciseDate);


        deleteBtn = (ImageButton) view.findViewById(R.id.deleteMsg);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //inflating the layout that contains custom message
                View warningMsgView = thisFragment.inflater.inflate(R.layout.dialog_warning, viewGroup,false);
                TextView customText = (TextView) warningMsgView.findViewById(R.id.customMsg);

                customText.setText("The activity: \"" + commentValue + "\" will be lost! do you want to continue");

                AlertDialog.Builder warningDiagBuilder = new AlertDialog.Builder(thisFragment.getActivity());
                warningDiagBuilder.setView(warningMsgView).setMessage(R.string.dialog);
                warningDiagBuilder.setView(warningMsgView).setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (parentActivity != null) {
                                    //tablet
                                    parentActivity.deleteMessage(myId);
                                    // once deleted the fragment is disappeared
                                    thisFragment.getFragmentManager().popBackStack();

                                } else {
                                    Log.i("tag", "hello");
                                    Intent intent = new Intent();
                                    intent.putExtra("deleteMsgId", myId);
                                    //intent.putExtra("deleteDBMsgId", dbID);
                                    getActivity().setResult(10, intent);
                                    getActivity().finish();
                                }

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog1 = warningDiagBuilder.create();
                dialog1.show();


            }
        });
        return view;
    }
}
