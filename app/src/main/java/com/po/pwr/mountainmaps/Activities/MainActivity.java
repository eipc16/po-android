package com.po.pwr.mountainmaps.Activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Fragments.BadgeDisplayFragment;
import com.po.pwr.mountainmaps.Fragments.HikerSelectionFragment;
import com.po.pwr.mountainmaps.Fragments.TripListFragment;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.DrawerNameTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BadgeDisplayFragment.OnFragmentInteractionListener, TripListFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private int curr_fragment = 0;

    public static String hiker_id = "1";
    public static String request_address = "http://192.168.1.16:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu_drawer);
        actionBar.setTitle(getResources().getString(R.string.trips_drawer));

        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }

            Fragment firstFragment = new TripListFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, firstFragment).addToBackStack(null).commit();
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                DrawerNameTask task = (DrawerNameTask) new DrawerNameTask(getApplicationContext(), mDrawerLayout).execute(request_address + "/hikers/" + hiker_id + "/credentials");
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                if (menuItem.getTitle().equals(getResources().getString(R.string.trips_drawer)) && curr_fragment != 0) {
                    curr_fragment = 0;
                } else if (menuItem.getTitle().equals(getResources().getString(R.string.badges_drawer)) && curr_fragment != 1) {
                    curr_fragment = 1;
                } else if (menuItem.getTitle().equals(getResources().getString(R.string.settings_drawer)) && curr_fragment != 2) {
                    curr_fragment = 2;
                } else {
                    curr_fragment = -1;
                }

                if (curr_fragment != -1) {
                    Fragment fragment;

                    if (curr_fragment == 0) {
                        fragment = new TripListFragment();
                    } else if(curr_fragment == 1) {
                        fragment = new BadgeDisplayFragment();
                    } else {
                        fragment = new HikerSelectionFragment();
                    }

                    mDrawerLayout.setDrawerTitle(GravityCompat.START, menuItem.getTitle());

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    actionBar.setTitle(menuItem.getTitle());
                } else {
                    Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}