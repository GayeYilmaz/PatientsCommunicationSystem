package com.example.patientscomm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword  extends AppCompatActivity {
    private EditText editTextEmailPasswordReset;
    private Button buttonPasswordReset;
    FirebaseAuth resetAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmailPasswordReset=findViewById(R.id.editTextRestPassword);
        buttonPasswordReset=findViewById(R.id.buttonResetPassword);

        resetAuth=FirebaseAuth.getInstance();

        buttonPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email=editTextEmailPasswordReset.getText().toString().trim();
        if(email.isEmpty()){
            editTextEmailPasswordReset.setError("Email.is required!");
            editTextEmailPasswordReset.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailPasswordReset.setError("Please provide valid email!");
            editTextEmailPasswordReset.requestFocus();
            return;

        }
        resetAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check Your email to reset your password!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);

                }else{

                    String error = task.getException().getMessage();


                    Toast.makeText(ForgotPassword.this, error+"Try again!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
