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

public class Login extends AppCompatActivity implements View.OnClickListener{
    public EditText memail;
    public EditText mpassword;
    public Button login;
    public Button signUpLog;
    public TextView textViewForgotPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        memail=findViewById(R.id.loginUsernameText);
        mpassword=findViewById(R.id.loginPassword);
        login=findViewById(R.id.loginLoginButton);
        signUpLog=findViewById(R.id.loginSignUpButton);
        textViewForgotPassword=findViewById(R.id.textViewforgot);
        login.setOnClickListener(this);
        signUpLog.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginLoginButton:
                //startActivity(new Intent(this,X.class));
                login();
                break;
            case R.id.textViewforgot:
                break;
            case R.id.loginSignUpButton:
                //Intent signUp=new Intent(this,SignUp.class);
                //startActivity(signUp);
               startActivity(new Intent(this,SignUp.class));
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
                    startActivity(new Intent(getApplicationContext(),X.class));

                }else{
                   // Toast.makeText(Login.this, "Error! "+task.getException().getMessage()  , Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}