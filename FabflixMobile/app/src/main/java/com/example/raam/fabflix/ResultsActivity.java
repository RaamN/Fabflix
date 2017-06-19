package com.example.raam.fabflix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ResultsActivity extends AppCompatActivity {
    private int increment = 0;
    private int NUM_ITEMS_PAGE = 10;
    private int pageCount = 0;
    private Button btn_prev;
    private Button btn_next;
    private ArrayList<String> data;

    private Bundle bundle;
    private String sResults;
    private String query;
    private String[] r;
    //        mainListView = (ListView) findViewById( R.id.resultList );
//    r = sResults.split("\n");
    private ArrayList<String> movieList = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private ListView mainListView;
    private TextView noResultListView;
//    Bundle bundle = getIntent().getExtras();
//    String sResults = bundle.getString("searchResults");
//    String [] r = sResults.split("\n");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //LOADLIST FUNCTIONS
        bundle = getIntent().getExtras();
        sResults = bundle.getString("searchResults");
        query = bundle.getString("query");


        mainListView = (ListView) findViewById( R.id.resultList );
        r = sResults.split("\n");
//        movieList = new ArrayList<String>();
//        movieList.addAll( Arrays.asList(r) );
//        listAdapter = new ArrayAdapter<String>(this, R.layout.a_layout_file, movieList);
//        mainListView.setAdapter(listAdapter);

        noResultListView = (TextView) findViewById(R.id.no_results);
        if (sResults.length() == 0)
        {
            System.out.println(r.length);
            System.out.println("Response: " + sResults);
            System.out.println("Response length: " + sResults.length());
            noResultListView.setVisibility(View.VISIBLE);

            btn_prev     = (Button)findViewById(R.id.prev);
            btn_next     = (Button)findViewById(R.id.next);
            btn_prev.setEnabled(false);
            btn_next.setEnabled(false);
        } else {
            System.out.println(r.length);
            System.out.println("Response: " + sResults);
            System.out.println("Response length: " + sResults.length());
            noResultListView.setVisibility(View.GONE);
            //COPY PASTE BELOW
            btn_prev     = (Button)findViewById(R.id.prev);
            btn_next     = (Button)findViewById(R.id.next);
            btn_prev.setEnabled(false);
            //data = new ArrayList<String>();

            /**
             * this block is for checking the number of pages
             * ====================================================
             */

            int TOTAL_LIST_ITEMS = r.length;
            int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
            val = val==0?0:1;
            pageCount = TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

            if (r.length < NUM_ITEMS_PAGE)
            {
                btn_next.setEnabled(false);
            }
            /**
             * =====================================================
             */

            /**
             * The ArrayList data contains all the list items
             */
//        for(int i=0;i<TOTAL_LIST_ITEMS;i++)
//        {
//            movieList.add("This is Item "+(i+1));
//        }
            movieList.addAll(Arrays.asList(r));
            loadList(0);



            btn_next.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    increment++;
                    loadList(increment);
                    CheckEnable();
                }
            });

            btn_prev.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    increment--;
                    loadList(increment);
                    CheckEnable();
                }
            });
        }


    }
    /**
     * Method for enabling and disabling Buttons
     */
    private void CheckEnable()
    {
        if(increment+1 == pageCount)
        {
            btn_next.setEnabled(false);
        }
        else if(increment == 0)
        {
            btn_prev.setEnabled(false);
        }
        else
        {
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
        }
    }

    /**
     * Method for loading data in listview
     * @param number
     */
    private void loadList(int number)
    {
        ArrayList<String> sort = new ArrayList<String>();

            int start = number * NUM_ITEMS_PAGE;
            for (int i = start; i < (start) + NUM_ITEMS_PAGE; i++) {
                if (i < movieList.size()) {
                    sort.add(movieList.get(i));
                } else {
                    break;

                }
            }

        listAdapter = new ArrayAdapter<String>(this, R.layout.a_layout_file, sort);

        mainListView.setAdapter(listAdapter);

    }


}
