package com.example.patientscomm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.patientscomm.Adapter.QuestionAdapter;
import com.example.patientscomm.Model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class X extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private SearchView searchBar;
    private TextView textViewAsk;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    EditText question_popup_editTextAsk;
    private Button question_popup_buttonAsk;
    private Button question_popup_buttonClose;

    private RecyclerView recyclerViewQuestion;
    private QuestionAdapter adapter;
    DatabaseReference databaseQuestion;
    private ArrayList<Question> list;
    private QuestionAdapter.ItemClickListener mItemListener;
    private TextView questionTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x);

        searchBar = findViewById(R.id.searchViewSearchBar);

        textViewAsk = findViewById(R.id.textViewAsk);
        textViewAsk.setOnClickListener(this);
        questionTextView=findViewById(R.id.showQuestion);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        /**--------------------Toolbar------------------------*/
        setSupportActionBar(toolbar);

        /**--------------------Navigation Drawer Menu------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        databaseQuestion = FirebaseDatabase.getInstance().getReference("questions");
        recyclerViewQuestion = findViewById(R.id.questionResult);

    }



    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewQuestion.setAdapter(adapter);
    }
    private void setAdapter(ArrayList<Question> list){
        setOnCliclListener();
        QuestionAdapter adapter = new QuestionAdapter(list,mItemListener);
        recyclerViewQuestion.setAdapter(adapter);
    }

    private void setOnCliclListener() {
        mItemListener = new QuestionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), CommentPage.class);
                intent.putExtra("question",list.get(position).getQuestion());
                intent.putExtra("questionId",list.get(position).getId());
                startActivity(intent);

             }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(databaseQuestion != null){
            databaseQuestion.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            list.add(ds.getValue(Question.class));

                        }

                        QuestionAdapter adapter = new QuestionAdapter(list);
                        recyclerViewQuestion.setAdapter(adapter);
                        //setOnCliclListener();
                       // recyclerViewQuestion.setAdapter(adapter);
                        setAdapter(list);


                    }
                    if(searchBar != null){
                        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                search(newText);
                                return false;
                            }
                        });
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                   // Toast.makeText(X.this,error.getMessage(),Toast.LENGTH_LONG);

                }
            });
        }

    }

    private void search(String str) {
        ArrayList<Question> myList = new ArrayList<>();
        for(Question object : list){
            if(object.getQuestion().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }


        QuestionAdapter adapter = new QuestionAdapter(myList);
        recyclerViewQuestion.setAdapter(adapter);
        setAdapter(myList);


       // setAdapter();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_message:
                Intent i = new Intent(X.this, Message.class);
                startActivity(i);
                break;
            case R.id.nav_setting:
                Intent i1 = new Intent(X.this, Setting.class);
                startActivity(i1);
                break;
            case R.id.nav_profile:
                Intent i2 = new Intent(X.this, Profile.class);
                startActivity(i2);
                break;

            case R.id.nav_logout:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }

    public void ask() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popupask, null);

        question_popup_editTextAsk = contactPopupView.findViewById(R.id.questionEditTextPopUp);
        question_popup_buttonAsk = contactPopupView.findViewById(R.id.buttonAsk);
        question_popup_buttonClose = contactPopupView.findViewById(R.id.buttonClose);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        question_popup_buttonAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
                dialog.dismiss();
            }
        });

        question_popup_buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void saveQuestion() {
        String question1="";
        String id = databaseQuestion.push().getKey();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        question1 = question_popup_editTextAsk.getText().toString().trim();
        Question question = new Question(id, userId, question1);
        databaseQuestion.child(id).setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
                else{
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewAsk:
                ask();
                break;

        }
    }



}