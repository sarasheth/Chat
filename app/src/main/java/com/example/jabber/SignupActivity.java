package com.example.jabber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jabber.Models.User;
import com.example.jabber.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide(); //hide tool bar in screen during signing in
        auth = FirebaseAuth.getInstance();   // Initialize Firebase Auth
        database = FirebaseDatabase.getInstance(); // Initialize Firebase Db

        progressDialog=new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating Account"); //title
        progressDialog.setMessage("We're creating your Account"); //show message

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //click on button signup
                progressDialog.show(); // when to show nd when not to
                auth.createUserWithEmailAndPassword
                        (binding.edEmail.getText().toString(),binding.edPassword.getText().toString()) //email nd password check
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)  //checks exception is present or not
                    {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();//closes the progressbar
                            User user= new User(binding.edUserName.getText().toString(),binding.edEmail.getText().toString(),
                                    binding.edPassword.getText().toString());

                            String id =task.getResult().getUser().getUid(); //create data to save uid of every individual registered person
                            database.getReference().child("Users").child(id).setValue(user);    //go to database create new child ref banega nd child will get in db
                            Toast.makeText(SignupActivity.this, "User created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignupActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,SignIn.class);
                startActivity(intent);
            }
        });

    }
}