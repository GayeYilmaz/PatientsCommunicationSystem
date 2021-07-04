package com.example.patientscomm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.patientscomm.Fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;


public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    DatabaseReference databaseProfile;

    TextView profileName;
    TextView profileSurname;
    TextView profileGender;
    TextView profileDisease;
    TextView profileDoctor;
    TextView profileHospital;
    TextView profileEmail;
    TextView profilePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout =findViewById(R.id.drawer_layout_profile);
        navigationView =findViewById(R.id.nav_view);
        toolbar =findViewById(R.id.toolbar);

        /**--------------------Toolbar------------------------*/
        setSupportActionBar(toolbar);
        /**--------------------Navigation Drawer Menu------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        TabLayout tabLayout = findViewById(R.id.profile_tab_layout);
        ViewPager viewPager = findViewById(R.id.profile_view_pager);

        Message.ViewPagerAdapter viewPagerAdapter = new Message.ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);







    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        }
        else{
            super.onBackPressed();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                Intent i = new Intent(Profile.this, X.class);
                startActivity(i);
                break;
            case R.id.nav_message:
                Intent i2 = new Intent(Profile.this, Message.class);
                startActivity(i2);

                break;
            case R.id.nav_setting:
                Intent i1 = new Intent(Profile.this, Setting.class);
                startActivity(i1);
                break;
            case R.id.nav_profile:

                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}