package com.example.diemsct;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;
    public static JSONArray jsonArray;
    ProgressBar progressBar;
    int timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivity_notification);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(NotificationActivity.this);
        String url = getString(R.string.IP) + "/notices";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            timer = 1;
                            jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            jsonArray = null;
                        }

                        setupViewPager(viewPager);

                        //Adding onTabSelectedListener to swipe views
                        tabLayout.addOnTabSelectedListener(NotificationActivity.this);
                        tabLayout.setupWithViewPager(viewPager);

                        progressBar.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonArray = null;
            }
        });

        timer = 0;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(timer != 1)
                {
//                    viewPager.setVisibility(View.GONE);
                    new MaterialDialog.Builder(NotificationActivity.this)
                            .title("Error")
                            .content("Please check internet connection and try again later")
                            .positiveText("Ok")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        },15000);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Notification");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        //Initializing the tablayout
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        //Initializing viewPager
//        viewPager = (ViewPager) findViewById(R.id.pager);
//        setupViewPager(viewPager);
//
//        //Adding onTabSelectedListener to swipe views
//        tabLayout.addOnTabSelectedListener(this);
//        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setupViewPager(ViewPager viewPager) {
        NotificationActivity.ViewPagerAdapter adapter = new NotificationActivity.ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new NotificationAll(), "All");
        adapter.addFragment(new NotificationFE(), "FE");
        adapter.addFragment(new NotificationCSE(), "CSE");
        adapter.addFragment(new NotificationMech(), "MECH");
        adapter.addFragment(new NotificationENTC(), "E&TC");
        adapter.addFragment(new NotificationCivil(), "CIVIL");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
