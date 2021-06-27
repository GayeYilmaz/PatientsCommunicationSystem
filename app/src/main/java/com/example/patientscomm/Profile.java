package com.example.patientscomm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        profileName=findViewById(R.id.profileName);
        profileSurname=findViewById(R.id.profileSurname);
        profileGender=findViewById(R.id.profileGender);
        profileDisease=findViewById(R.id.profileDisease);
        profileDoctor=findViewById(R.id.profileDoctor);
        profileHospital=findViewById(R.id.profileHospital);
        profileEmail=findViewById(R.id.profileEmail);
        profilePassword=findViewById(R.id.profilePassword);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseProfile = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        databaseProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileName.setText(snapshot.child("name").getValue().toString());
                profileSurname.setText(snapshot.child("surname").getValue().toString());
                profileGender.setText(snapshot.child("gender").getValue().toString());
                profileDisease.setText(snapshot.child("disease").getValue().toString());
                profileDoctor.setText(snapshot.child("doctor").getValue().toString());
                profileHospital.setText(snapshot.child("hospital").getValue().toString());
                profileEmail.setText(snapshot.child("email").getValue().toString());
                profilePassword.setText(snapshot.child("password").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                Intent i = new Intent(Profile.this,X.class);
                startActivity(i);
                break;
            case R.id.nav_message:
                Intent i2 = new Intent(Profile.this,Message.class);
                startActivity(i2);

                break;
            case R.id.nav_setting:
                Intent i1 = new Intent(Profile.this,Setting.class);
                startActivity(i1);
                break;
            case R.id.nav_profile:

                break;
            case R.id.nav_share:
                Toast.makeText(this,"Share",Toast.LENGTH_LONG).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}