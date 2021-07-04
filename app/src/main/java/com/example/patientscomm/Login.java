package com.example.patientscomm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    public EditText memail;
    public EditText mpassword;
    public Button login;
    public Button signUpLog;
    public TextView textViewForgotPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        memail=findViewById(R.id.loginUsernameText);
        mpassword=findViewById(R.id.loginPassword);
        login=findViewById(R.id.loginLoginButton);
        login.setOnClickListener(this);
        signUpLog=findViewById(R.id.loginSignUpButton);
        signUpLog.setOnClickListener(this);
        textViewForgotPassword=findViewById(R.id.textViewforgot);
        textViewForgotPassword.setOnClickListener(this);
        login.setOnClickListener(this);
        signUpLog.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();




}

    @Override
    protected void onStart() {
        super.onStart();
        //If the user is not logout.
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser !=null){
            Intent intentX = new Intent(Login.this, X.class);
            startActivity(intentX);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginLoginButton:
                //startActivity(new Intent(this,X.class));
                login();
                break;
            case R.id.textViewforgot:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            case R.id.loginSignUpButton:
                //Intent signUp=new Intent(this,SignUp.class);
                //startActivity(signUp);
                startActivity(new Intent(this, SignUp.class));
                break;


        }
}

    private void login() {
        String email = memail.getText().toString().trim();
        String password = mpassword.getText().toString().trim();

        if(email.isEmpty()){
            memail.setError("Email is required!");
            memail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            memail.setError("Please provide valid email!");
            memail.requestFocus();
            return;

        }
        if(password.isEmpty()){
            mpassword.setError("Password is required!");
            mpassword.requestFocus();
            return;

        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                       startActivity(new Intent(getApplicationContext(),X.class));

                    }else{
                        user.sendEmailVerification();

                        Toast.makeText(Login.this, "Check your email to verify your account"  , Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(Login.this, "Wrong email or password"  , Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}