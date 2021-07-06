package com.example.patientscomm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.patientscomm.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText editTextName;
    EditText editTextSurname;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordRepeat;
    EditText editTextDisease;
    EditText editTextDoctor;
    EditText editTextHospital;
    Button buttonSignUp;
    String stringGender;
    Button buttonSignUpBack;

    DatabaseReference databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextName = findViewById(R.id.signUpName);
        editTextSurname = findViewById(R.id.signUpSurname);
        editTextDoctor = findViewById(R.id.signUpDoctor);
        editTextHospital = findViewById(R.id.signUpDoctor);
        editTextDisease = findViewById(R.id.signUpDisease);
        editTextEmail = findViewById(R.id.signUpEmail);
        editTextPassword = findViewById(R.id.signUpPassword);
        editTextPasswordRepeat = findViewById(R.id.signUpRepPassword);
        buttonSignUp = findViewById(R.id.signUpsignUpButton);
        buttonSignUpBack = findViewById(R.id.signUpBack);
        buttonSignUp.setOnClickListener(this);
        buttonSignUpBack.setOnClickListener(this);
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.female:
                if (checked)
                    stringGender = "female";
                break;
            case R.id.male:
                if (checked)
                    stringGender = "male";
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signUpsignUpButton:
                signUp();
                break;
            case R.id.signUpBack:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }

    private void signUp(){
        String name = editTextName.getText().toString().trim();
        String surname= editTextSurname.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordRepeat = editTextPasswordRepeat.getText().toString().trim();
        String hospital= editTextDoctor.getText().toString().trim();
        String doctor = editTextDoctor.getText().toString().trim();
        String disease= editTextDisease.getText().toString().trim();
        String imageURL="dafault";
        String status = "online";
        if(name.isEmpty()){
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }
        if(surname.isEmpty()){
            editTextSurname.setError("Surname is required!");
            editTextSurname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;

        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;

        }
        if(password.length()<6){
            editTextPassword.setError("Min password should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.equals(passwordRepeat)){
            editTextPasswordRepeat.setError("Don't match with the password!");
            editTextPasswordRepeat.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    //databaseUser.push().getKey();
                            String search = name.toLowerCase();
                            User user = new User(id,name,surname,doctor,hospital,disease,password,email,stringGender,imageURL,status,search);
                            databaseUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this,"Successful!",Toast.LENGTH_LONG).show();


                                        Intent x = new Intent(SignUp.this, X.class);
                                        startActivity(x);
                                    }else{
                                        Toast.makeText(SignUp.this,"Try again!Something wrong happened!",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }else{


                        }
                    }
                });


    }
}