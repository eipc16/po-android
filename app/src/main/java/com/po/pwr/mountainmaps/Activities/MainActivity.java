package com.po.pwr.mountainmaps.Activities;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Fragments.Badge.BadgeDisplayFragment;
import com.po.pwr.mountainmaps.Fragments.Settings.HikerSelectionFragment;
import com.po.pwr.mountainmaps.Fragments.HikingTrails.HikingTrailListFragment;
import com.po.pwr.mountainmaps.Models.HikerViewModel;
import com.po.pwr.mountainmaps.Models.PointViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    public int curr_fragment = 0;

    public static Integer hiker_id = 1;
    public static String request_address = "http://192.168.1.104:8080";

    public RestTemplate restTemplate;

    @SuppressLint("UseSparseArrays")
    public final static Map<Integer, PointViewModel> pointSet = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRestTemplate();

        setUpStartView(savedInstanceState);
    }

    public void setUpStartView(Bundle savedInstanceState) {
        final ActionBar actionBar = getSupportActionBar();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_drawer);
            actionBar.setTitle(getResources().getString(R.string.trips_drawer));
        }

        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }

            Fragment firstFragment = new HikingTrailListFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, firstFragment).addToBackStack(null).commit();
        }

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                //Do something when drawer is sliding
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                loadHiker(hiker_id);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                //Do something when drawer is closed
            }

            @Override
            public void onDrawerStateChanged(int i) {
                //Do something when drawer state is changed
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
                        fragment = HikingTrailListFragment.newInstance(getResources().getString(R.string.trips_drawer));
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

    public void setRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

    }

    public void loadPoints() {
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<List<PointViewModel>>() {

            @Override
            public ResponseEntity<List<PointViewModel>> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, new ParameterizedTypeReference<List<PointViewModel>>() {
                });
            }

            @Override
            public void onTaskExecuted(ResponseEntity<List<PointViewModel>> result) {
                List<PointViewModel> pointList = result.getBody();

                for (PointViewModel pm: pointList) {
                    pointSet.put(pm.getId(), pm);
                }

            }

        }).execute(request_address + "points/all");
    }

    public void loadHiker(final Integer hikerId) {
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<HikerViewModel>() {

            @Override
            public ResponseEntity<HikerViewModel> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, HikerViewModel.class);
            }

            @Override
            public void onTaskExecuted(ResponseEntity<HikerViewModel> result) {
                HikerViewModel hikerViewModel = result.getBody();

                if(hikerViewModel != null) {
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUser = headerView.findViewById(R.id.nav_title);

                    navUser.setText(hikerViewModel.toString());
                }
            }
        }).execute(request_address + "/hikers/" + hikerId + "/credentials/");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}