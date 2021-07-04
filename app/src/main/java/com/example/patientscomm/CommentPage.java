package com.example.patientscomm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientscomm.Adapter.CommentAdapter;
import com.example.patientscomm.Model.Comment;
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

public class CommentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    EditText comment_popup_edittext;
    private Button comment_popup_button;
    private Button comment_popup_closebutton;

    private Button commentButton;
    private TextView questionTextView;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> list = new ArrayList<>();
    private CommentAdapter.CommentClickListener commentClickListener;
    String questionId;
    DatabaseReference databaseComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentButton=findViewById(R.id.comment);
        commentButton.setOnClickListener(this);
        questionTextView = findViewById(R.id.showQuestion);
        commentRecyclerView = findViewById(R.id.commentList);


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

        databaseComment = FirebaseDatabase.getInstance().getReference("comments");
        Bundle bundle = getIntent().getExtras();

        String question="";
        if(bundle != null){
            question = bundle.getString("question");
            questionId =bundle.getString("questionId");
            System.out.println(question);
            System.out.println(questionId);
        }


        questionTextView.setText(question);

    }

    @Override
    protected void onResume() {
        super.onResume();
        commentRecyclerView.setAdapter(commentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(databaseComment != null){
            databaseComment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list=new ArrayList<>();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(questionId.equals(ds.child("questionId").getValue().toString())){
                                list.add(ds.getValue(Comment.class));
                            }


                        }
                        setAdapter();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setAdapter() {
        setOnClickListener();
        CommentAdapter commentAdapter = new CommentAdapter(list,commentClickListener);
        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void setOnClickListener() {
        commentClickListener = new CommentAdapter.CommentClickListener() {
            @Override
            public void onCommentClick(View v, int position) {

            }
        };
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.comment:
                comment();
                break;

        }
    }

    private void comment() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popupcomment, null);

        comment_popup_edittext= contactPopupView.findViewById(R.id.commentEditTextPopUp);
        comment_popup_button = contactPopupView.findViewById(R.id.buttonComment);
        comment_popup_closebutton=contactPopupView.findViewById(R.id.commentCloseButton);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        comment_popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
                dialog.dismiss();
            }
        });
        comment_popup_closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void saveComment() {
        String comment1="";
        String id= databaseComment.push().getKey();
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        comment1 = comment_popup_edittext.getText().toString().trim();
        Comment comment = new Comment(id,userId,questionId,comment1);
        databaseComment.child(id).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else{

                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_message:
                Intent i = new Intent(CommentPage.this, Message.class);
                startActivity(i);
                break;
            case R.id.nav_setting:
                Intent i1 = new Intent(CommentPage.this, Setting.class);
                startActivity(i1);
                break;
            case R.id.nav_profile:
                Intent i2 = new Intent(CommentPage.this, Profile.class);
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
}
