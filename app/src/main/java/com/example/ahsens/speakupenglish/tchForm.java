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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class tchForm extends AppCompatActivity {

    EditText fnameTch, unameTch, mailTch, pwdTch, cpwdTch, hrs, min, sec;
    Button regTch;
    RadioButton male,fmale,uk,us,bs,ms,phd;

    DatabaseReference dbref;

    FirebaseAuth fbauth;
    FirebaseAuth.AuthStateListener auth_listner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_form);

        getSupportActionBar().setTitle("Signup as Teacher");


        fnameTch = (EditText) findViewById(R.id.fnameTch);
        unameTch = (EditText) findViewById(R.id.unameTch);
        mailTch = (EditText) findViewById(R.id.mailTch);
        pwdTch = (EditText) findViewById(R.id.pwdTch);
        cpwdTch = (EditText) findViewById(R.id.cpwdTch);
        //hrs = (EditText) findViewById(R.id.hrs);
        //min = (EditText) findViewById(R.id.min);
        //sec = (EditText) findViewById(R.id.sec);
        regTch = (Button) findViewById(R.id.regTch);
        //rg1 = (RadioGroup) findViewById(R.id.rg1);
        //rg2 = (RadioGroup) findViewById(R.id.rg2);
        //rg3 = (RadioGroup) findViewById(R.id.rg3);
        //rg1 = (RadioGroup) findViewById(R.id.rg1);
        //male = (RadioButton) findViewById(R.id.male);
        //fmale = (RadioButton) findViewById(R.id.fmale);
        uk = (RadioButton) findViewById(R.id.uk);
        us = (RadioButton) findViewById(R.id.us);
        bs = (RadioButton) findViewById(R.id.bs);
        ms = (RadioButton) findViewById(R.id.ms);
        phd = (RadioButton) findViewById(R.id.phd);



        dbref = FirebaseDatabase.getInstance().getReference("User");

        //Get Firebase auth instance
        fbauth = FirebaseAuth.getInstance();


        regTch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String email = mailTch.getText().toString().trim();
                String password = pwdTch.getText().toString().trim();
                String cpassword = cpwdTch.getText().toString().trim();

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
                    Toast.makeText(tchForm.this, "Password doesn't match! ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }



                // making obj and set dta
                final teacherData teacherData = new teacherData();

                teacherData.setFullName(fnameTch.getText().toString());
                if (TextUtils.isEmpty(fnameTch.getText().toString().trim())) {
                    Toast.makeText(tchForm.this, "FullName?", Toast.LENGTH_SHORT).show();
                    return;
                }

                teacherData.setUserName(unameTch.getText().toString());
                if (TextUtils.isEmpty(unameTch.getText().toString().trim())) {
                    Toast.makeText(tchForm.this, "UserName?", Toast.LENGTH_SHORT).show();
                    return;
                }

                teacherData.setEmail(mailTch.getText().toString());
                if (TextUtils.isEmpty(mailTch.getText().toString().trim())) {
                    Toast.makeText(tchForm.this, "Email?", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (uk.isSelected() ) {
                    Toast.makeText(tchForm.this, "Origin is: UK ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (us.isSelected() ) {
                    Toast.makeText(tchForm.this, "Origin is: US ", Toast.LENGTH_SHORT).show();
                    return;
                }

                teacherData.setPwd(pwdTch.getText().toString());
/*
                if (male.isChecked() == true && fmale.isChecked() == false){

                    teacherData.setGender(male.getText().toString());

                }
                else if (male.isChecked() == false && fmale.isChecked() == true){

                    teacherData.setGender(fmale.getText().toString());

                }



                if (uk.isChecked() == true && us.isChecked() == false){

                    teacherData.setOrigin(uk.getText().toString());

                }
                else if (uk.isChecked() == false && us.isChecked() == true){

                    teacherData.setOrigin(us.getText().toString());

                }

*/

                if (bs.isChecked() == true && ms.isChecked() == false && phd.isChecked() == false){

                    teacherData.setQualification(bs.getText().toString());

                }
                else if (bs.isChecked() == false && ms.isChecked() == true && phd.isChecked() == false){

                    teacherData.setQualification(ms.getText().toString());

                }
                else if (bs.isChecked() == false && ms.isChecked() == false && phd.isChecked() == true){

                    teacherData.setQualification(phd.getText().toString());

                }


        /*
                teacherData.setAvailability(hrs.getText().toString());
                if (TextUtils.isEmpty(hrs.getText().toString().trim())) {
                    Toast.makeText(tchForm.this, "Hrs must be selected!", Toast.LENGTH_SHORT).show();
                    return;
                }

                    teacherData.setAvailability(min.getText().toString());
                    if (TextUtils.isEmpty(min.getText().toString().trim())) {
                        Toast.makeText(tchForm.this, "Mins must be selected!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    teacherData.setAvailability(sec.getText().toString());
        */

                teacherData.setUnique(getIntent().getStringExtra("fchoice"));



                //create user
                fbauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(tchForm.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                dbref.child(fbauth.getCurrentUser().getUid()).setValue(teacherData);

                                // intent here

                                Intent i = new Intent(getApplicationContext(), tchMenu.class);
                                startActivity(i);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();

                               // Toast.makeText(tchForm.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                //progressBar.setVisibility(View.GONE);

                               // Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                //startActivity(i);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(tchForm.this, "Authentication failed." + task.getException(),
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
