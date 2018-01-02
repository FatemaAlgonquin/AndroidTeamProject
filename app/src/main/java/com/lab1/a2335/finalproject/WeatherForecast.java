package com.lab1.a2335.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends ToolbarActivity {
    protected static final String TAG = "WeatherForecast";
    private ProgressBar progressBar;

    WebView mWebView;

    // reference to menu bar.
    Menu bottomMenubar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_page_load_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWebView = (WebView) findViewById(R.id.webView_content);
        mWebView.getSettings().setJavaScriptEnabled(true);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://www.runnersworld.com/motivation/12-habits-to-keep-up-your-running-motivation/slide/2";
        ForecastQuery forecast = new ForecastQuery();
        forecast.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // call the parent method.
        super.onCreateOptionsMenu(menu);

        // disable the async menu item since we are already in the async activity page.
        MenuItem asynItemMenu =  menu.findItem(R.id.action_three);
        asynItemMenu.setEnabled(false);
        asynItemMenu.setChecked(true);
        return true;
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        String min;
        String max;
        String currentTemp;
        String iconName;
        Bitmap icon;

        @Override
        protected void onPreExecute(){
            Log.i(TAG,"Starting AsyncTask");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            Log.i(TAG, "In doInBackgroud");
           // HttpURLConnection connection = null;
            try{
                URL dataUrl = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)dataUrl.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.connect();
                int status = connection.getResponseCode();
                Log.d("connection", "status " + status);
                InputStream is = connection.getInputStream();

                if (status == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    String line;
                    int progress = 10;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                        publishProgress(progress++);
                    }
                }

            }catch(Exception e ){
                Log.i(TAG, "filename " );

            }
            return result;
        }

        public void onProgressUpdate(Integer... value){
            Log.i(TAG, "in onProgressUpdate");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }



        public void onPostExecute(String result){
            updateWebView(result);
            progressBar.setVisibility(View.INVISIBLE);
        }



    }

    public boolean isFileExists(String fileName){
        File file = getBaseContext().getFileStreamPath(fileName);
        Log.i(TAG, file.toString());

        return file.exists();

    }

    public Bitmap getImage(URL url){
        Log.i(TAG, "In getImage");
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection)url.openConnection();
            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                Log.i(TAG, "downloading image");
                Bitmap bm = BitmapFactory.decodeStream(connection.getInputStream());
                return bm;

            }
            else{
                return null;
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
           if (connection != null){
               connection.disconnect();
           }
        }


    }

    private void updateWebView(String result) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(result, "text/html; charset=utf-8", "utf-8");
    }

}
