package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class stdForm extends AppCompatActivity {


    EditText fnameStd, unameStd, mailStd, pwdStd, cpwdStd;
    Button regStd;
    ScrollView scrollView;

    DatabaseReference dbref;

    FirebaseAuth fbauth;
    FirebaseAuth.AuthStateListener auth_listner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_form);

        getSupportActionBar().setTitle("Signup as Student");

        scrollView = (ScrollView) findViewById(R.id.sv);
        fnameStd = (EditText) findViewById(R.id.fnameStd);
        unameStd = (EditText) findViewById(R.id.unameStd);
        mailStd = (EditText) findViewById(R.id.mailStd);
        pwdStd = (EditText) findViewById(R.id.pwdStd);
        cpwdStd = (EditText) findViewById(R.id.cpwdStd);
        regStd = (Button) findViewById(R.id.regStd);

        dbref = FirebaseDatabase.getInstance().getReference("User");

        //Get Firebase auth instance
        fbauth = FirebaseAuth.getInstance();


        regStd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String email = mailStd.getText().toString().trim();
                String password = pwdStd.getText().toString().trim();
                String cpassword = cpwdStd.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(cpassword)) {
                    Toast.makeText(getApplicationContext(), "Confirm password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Objects.equals(password, cpassword)) {
                    Toast.makeText(stdForm.this, "Password doesn't match! ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                    // making obj and set dta
                final studentData studentData = new studentData();

                studentData.setFullName(fnameStd.getText().toString());
                if (TextUtils.isEmpty(fnameStd.getText().toString().trim())) {
                    Toast.makeText(stdForm.this, "Full name?", Toast.LENGTH_SHORT).show();
                    return;
                }

                studentData.setUserName(unameStd.getText().toString());
                if (TextUtils.isEmpty(unameStd.getText().toString().trim())) {
                    Toast.makeText(stdForm.this, "User name?", Toast.LENGTH_SHORT).show();
                    return;
                }

                studentData.setEmail(mailStd.getText().toString());
                if (TextUtils.isEmpty(mailStd.getText().toString().trim())) {
                    Toast.makeText(stdForm.this, "Email?", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                studentData.setPwd(pwdStd.getText().toString());
                if (TextUtils.isEmpty(pwdStd.getText().toString().trim())) {
                    Toast.makeText(stdForm.this, "Full name?", Toast.LENGTH_SHORT).show();
                    return;
                }
                */
                studentData.setUnique(getIntent().getStringExtra("fchoice"));



                    // setting data to db using obj (created)



                //create user
                fbauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(stdForm.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                dbref.child(fbauth.getCurrentUser().getUid()).setValue(studentData);


                                // intent here

                                Intent i = new Intent(getApplicationContext(), stdMenu.class);
                                startActivity(i);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();

                                //Toast.makeText(stdForm.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                               // Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                //startActivity(i);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(stdForm.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        }

}