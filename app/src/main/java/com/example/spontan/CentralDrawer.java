package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CentralDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, RecommendedActivityText {

    private DrawerLayout drawer;
    private TextView optionsBtn, name, email;
    private NavigationView navigationView;
    DbHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
         myDb = Constants.getMyDBHelper(this);
        drawer = (DrawerLayout) findViewById(R.id.central_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

                name = (TextView) drawerView.findViewById(R.id.textViewNavName);
                email = (TextView) drawerView.findViewById(R.id.textViewNavEmail);

                Cursor res = myDb.getAllData();
                if(res.getCount() == -1) {
                    // show message
                    // showMessage("Error","Nothing found");
                    System.out.println("No data");
                    return;
                }

                //StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {

                    name.setText(res.getString(0));
                    email.setText(res.getString(1));
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }

        optionsBtn = findViewById(R.id.optionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerVisible(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                else{
                    drawer.openDrawer(GravityCompat.START);

                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println("Inside onOptionsItemSelected");
        navigationView.setCheckedItem(R.id.nav_dark_mode);
        navigationView.getMenu().performIdentifierAction(R.id.nav_dark_mode, 0);

        if(drawer.isDrawerVisible(GravityCompat.START)){
            System.out.println("Drawer visible returns true on onOptionsItemSelected");
            return true;
        }
        else {
            System.out.println("Drawer visible returns super() on onOptionsItemSelected");
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchActivityFragment()).commit();
                break;
            case R.id.nav_recommended:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecommendedActivityFragment()).commit();
                break;
            case R.id.nav_recommended_grp:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecommendedGroupFragment()).commit();
                break;
            case R.id.nav_upcoming:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpcomingActivityFragment()).commit();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_dark_mode:
                if (Constants.isNightModeActive(getApplicationContext())){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(getApplicationContext(), "Dark Mode Disabled", Toast.LENGTH_SHORT);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(getApplicationContext(), "Dark Mode Enabled", Toast.LENGTH_SHORT);
                }
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen((GravityCompat.START))){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }



    @Override
    public void setResult(String str) {
        // leave empty
    }

    @Override
    public void setFragmentResult(Fragment fragment, String str) {
        ((RecommendedActivityText)fragment).setResult(str);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}