package com.lab1.a2335.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyList extends ToolbarActivity {
    //TextView comment;
    EditText commentText;
    //TextView time;
    EditText timeText;
    FrameLayout frameLayout;

    //TextView displayText;

    String TAG = "DailyList";
    Button okButton;


    String displayComment;
    String duration;
    final ArrayList<ExerciseDAO> exerciseDAO = new ArrayList<ExerciseDAO>();

    Cursor cursor;
    boolean isTab;
    int deleteId;
    long deleteBDid;
    ExerciseListAdapter commentsAdapter;
    TextView detailLabel;


    DailyList ownActivity;
    ExerciseDatabaseHelper exerciseDatabaseHelper;
    ContentValues contentValues = new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Context context = getApplicationContext();

        final String selectedDateStr = getIntent().getStringExtra("SELECTED_DATE");

        // show date as January, 2008 as the title bar
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date originalDate = originalFormat.parse(selectedDateStr);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM, yyyy");
            String yearMonth = dateFormatter.format(originalDate);
            TextView view = (TextView) findViewById(R.id.textView);
            view.setText(view.getText()+": " + yearMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }






        frameLayout = (FrameLayout)findViewById(R.id.exerciseDetails);

        //phone
        if(frameLayout == null){
            Log.i(TAG, "frame is not loaded");
            isTab = false;
        }
        //tab
        else{
            Log.i(TAG, "frame is loaded");
            isTab = true;
            detailLabel = (TextView) findViewById(R.id.dailySummaryLabel);
            detailLabel.setVisibility(View.INVISIBLE);
        }


        commentText = (EditText)findViewById(R.id.editTextRunning);
        editTextOnFocusChangeListener(commentText);

        timeText = (EditText)findViewById(R.id.editTextRunningTime);
        editTextOnFocusChangeListener(timeText);

        List<String> convertString;
        ListView list = (ListView) findViewById(R.id.msgRunningListView);
        commentsAdapter = new ExerciseListAdapter(this);
        list.setAdapter(commentsAdapter);
        okButton = (Button)findViewById(R.id.addButtonRunning);
        exerciseDatabaseHelper = new ExerciseDatabaseHelper(context);
        exerciseDatabaseHelper.open();

        cursor = exerciseDatabaseHelper.getActivitiesByMonth(selectedDateStr);


        if(cursor.moveToFirst()){
            do{
                String comment = cursor.getString(cursor.getColumnIndex(exerciseDatabaseHelper.KEY_COMMENT));
                Log.i("DailyList Comment: ", comment);
                String duration = cursor.getString(cursor.getColumnIndex(exerciseDatabaseHelper.KEY_ACTIVITY_DURATION)).toString();
                Log.i("DailyList duration: ", duration);

                String dateStr = cursor.getString(cursor.getColumnIndex(exerciseDatabaseHelper.KEY_ACTIVITY_DATE));
                Log.i("DailyList date: ", duration);

                ExerciseDAO dao = new ExerciseDAO(comment, duration,dateStr);
                this.exerciseDAO.add(dao);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }


        Log.i(TAG, "Cursor's column: " + cursor.getColumnCount());
        for(int i = 0; i< cursor.getColumnCount();  i++){
            Log.i(TAG, cursor.getColumnName(i));

        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayComment = (commentText.getText().toString());
                Log.i("DailyList Comment: ", displayComment);
                duration = timeText.getText().toString();
                Log.i("DailyList time: ", duration);
                Toast.makeText(getBaseContext(),displayComment,Toast.LENGTH_SHORT).show();

                ExerciseDAO dao = new ExerciseDAO(displayComment, duration,selectedDateStr);
                exerciseDAO.add(dao);

                commentsAdapter.notifyDataSetChanged();
                commentText.setText("");
                timeText.setText("");

                contentValues.put(ExerciseDatabaseHelper.KEY_COMMENT,displayComment);
                contentValues.put(ExerciseDatabaseHelper.KEY_ACTIVITY_DURATION, duration);
                contentValues.put(ExerciseDatabaseHelper.KEY_ACTIVITY_DATE, selectedDateStr);
                contentValues.put("Activity",TAG.toLowerCase());

                exerciseDatabaseHelper.insert( contentValues);
                cursor = exerciseDatabaseHelper.getActivity(TAG);

            }
        });

        ownActivity = this;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = commentsAdapter.getItem(position);
                String str = (String) o; //Default String Adapter
                Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //Do some stuff

                }

                String duration  = commentsAdapter.getItemDuration(position);
                String dateStr = commentsAdapter.getItemDate(position);

                if(isTab){
                    detailLabel.setVisibility(View.VISIBLE);
                    // if the app is parentActivity on a tablet
                    MessageFragment fragment = new MessageFragment();
                    fragment.setParentActivity(ownActivity);
                    Bundle bundle = new Bundle();
                    bundle.putString("commentMsg", str);
                    bundle.putString("durationMsg", duration);
                    bundle.putString("dateMsg", dateStr);
                    bundle.putInt("Id",position);
                    //bundle.putLong("dbId",id);
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.exerciseDetails, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                /* sending the activity to the newly created MessageDetails class */
                else{
                    Intent intent = new Intent(getApplicationContext(), MessageDetails.class);
                    intent.putExtra("commentMsg",str);
                    intent.putExtra("Id", position);

                    intent.putExtra("durationMsg", duration);
                    intent.putExtra("dateMsg", dateStr);
                    //intent.putExtra("dbId",id);
                    startActivityForResult(intent, 10);
                }


            }

        });

    }

    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 10  && responseCode == 10) {
            // received data from fragment to delete the message
            Bundle bundle = data.getExtras();
            deleteId = bundle.getInt("deleteMsgId");
            //deleteBDid = bundle.getLong("deleteDBMsgId");
            deleteBDid = commentsAdapter.getItemId(deleteId);
            exerciseDatabaseHelper.remove(deleteBDid);
            exerciseDAO.remove(deleteId);

            cursor = exerciseDatabaseHelper.getChatMessages();
            commentsAdapter.notifyDataSetChanged();

        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide keyboard when focus is changed from edittext
     * @param editText
     */
    public void editTextOnFocusChangeListener(EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }


    public void deleteMessage(int id){
        long deleteDBIdTab = commentsAdapter.getItemId(id);
        exerciseDatabaseHelper.remove(deleteDBIdTab);
        exerciseDAO.remove(id);
        commentsAdapter.notifyDataSetChanged();

    }

    public void onDestroy(){
        super.onDestroy();
        this.exerciseDatabaseHelper.close();
    }


    //public class ExerciseListAdapter extends ArrayAdapter<String, String> {

    private class ExerciseListAdapter extends ArrayAdapter<String> {


        public ExerciseListAdapter(Context c){
            super(c, 0);
        }

        @Override
        public int getCount() {
            return exerciseDAO.size();
        }

        @Override
        public String getItem(int position) {
            return exerciseDAO.get(position).getComment();
        }

        /**
         * Returns the duration of the activity identified by the position.
         * @param position index of the list
         * @return duration of this activity.
         */
        public String getItemDuration(int position) {
            return exerciseDAO.get(position).getDuration();
        }

        /**
         * Returns the date of the activity identified by the position.
         * @param position index of the list
         * @return date when this activity took place.
         */
        public String getItemDate(int position) {
            return exerciseDAO.get(position).getDate();
        }

        @Override
        //public long getItemId(int position) {
            //return position;
        //}

        public long getItemId(int position){
            cursor.moveToPosition(position);
            int  id = exerciseDatabaseHelper.getIdFromCursor(cursor);
            return id;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = DailyList.this.getLayoutInflater();
            View result = view;

            result = inflater.inflate(R.layout.comment_outgoing, viewGroup, false);

            TextView commentText = (TextView)result.findViewById(R.id.commentText);
            //
            if(isTab){
                commentText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
            }else{
                commentText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

            }


            commentText.setText( getItem(position));
            //ImageView imgView = (ImageView)result.findViewById(R.id.commentImg);
            return result;
        }
    }

    /**
     * Internal class to represent a database record.
     */
    private class ExerciseDAO{
        String comment;
        String duration;
        String dateStr;

        ExerciseDAO(String comment, String duration, String dateStr){
            this.comment = comment;
            this.duration = duration;
            this.dateStr = dateStr;
        }

        String getComment(){
            return this.comment;
        }

        String getDuration(){
            return this.duration;
        }

        String getDate(){ return this.dateStr;}


    }
}
