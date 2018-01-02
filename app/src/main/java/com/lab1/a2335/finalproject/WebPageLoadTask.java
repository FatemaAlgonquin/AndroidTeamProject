package com.lab1.a2335.finalproject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fatema on 2017-12-25.
 */

public class WebPageLoadTask extends Activity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_page_load_task);
        mWebView = (WebView) findViewById(R.id.webView_content);
        mWebView.getSettings().setJavaScriptEnabled(true);



        new SimpleTask().execute("https://gist.github.com/Phonbopit/09523e53eb8e38fb369b");
    }

    private class SimpleTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        protected String doInBackground(String... urls)   {
            String result = "";
            try {

                URL dataUrl = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection)dataUrl.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.connect();

                int status = connection.getResponseCode();
                Log.d("connection", "status " + status);


                if (status == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                }

            } catch (Exception e) {
                Log.i("ddd",e.toString());
            }
            return result;
        }

        protected void onPostExecute(String result)  {
            // Dismiss ProgressBar
            updateWebView(result);
        }
    }

    private void updateWebView(String result) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(result, "text/html; charset=utf-8", "utf-8");
    }


}
