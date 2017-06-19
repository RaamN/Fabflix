package com.example.raam.fabflix;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import java.nio.charset.Charset;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;


public class SearchActivity extends AppCompatActivity {
    private SearchActivity.SearchActivityTask mAuthTask = null;
    private SearchView mSearchView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String searchQ;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchView = (SearchView) findViewById(R.id.search_bar);

        Button mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Searching ...");
                attemptSearch();

            }
        });
    }

    private void attemptSearch() {
        System.out.println("hi");

        /*if (mAuthTask != null) {
            return;
        }*/


        // Reset errors.
        //mSearchView.setError(null);


        // Store values at the time of the login attempt.
        String title = mSearchView.getQuery().toString();
        System.out.println(title);
        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new SearchActivity.SearchActivityTask(title);
            mAuthTask.execute((Void) null);
        }
    }

    public class SearchActivityTask extends AsyncTask<Void, Void, Boolean>  {

        private final String mTitle;

        SearchActivityTask(String t) {
            mTitle = t;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                final Map<String, String> p = new HashMap<String, String>();
                // no user is logged in, so we must connect to the server
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String encodedString = mTitle;
                //final Context context = this;
                try {

                    encodedString = URLEncoder.encode(mTitle, "UTF-8");
                } catch (UnsupportedEncodingException e){
                    System.out.println(e.getMessage());
                }
                System.out.println(encodedString);
                String url = "http://10.0.2.2:8080/Fabflix/AppSearch?title=" + encodedString;

                // Simulate network access.
                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                                intent.putExtra("searchResults", response);
                                intent.putExtra("query", mTitle);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("security.error", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {

                        p.put(mTitle, mTitle);
                        System.out.print(mTitle);
                        return p;
                    }
                };
                queue.add(postRequest);


                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }
    }
}
