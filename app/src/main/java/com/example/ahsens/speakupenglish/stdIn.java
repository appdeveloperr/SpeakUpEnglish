package com.example.ahsens.speakupenglish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class stdIn extends AppCompatActivity {


    EditText name, mail, pwd;
    Button done;
    //ProgressDialog mProgress1,mProgress2;
    FirebaseAuth fbauthh;
    FirebaseAuth.AuthStateListener auth_listner;
    DatabaseReference activeUserss;
    FirebaseUser user;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_in);

        getSupportActionBar().setTitle("Signin as Student");

        activeUserss = FirebaseDatabase.getInstance().getReference("ActiveUser").child("Student");
       // mProgress2 = new ProgressDialog(this);
        //mProgress1 = new ProgressDialog(this);

        name = (EditText) findViewById(R.id.nameInStd);
        mail = (EditText) findViewById(R.id.mailInStd);
        pwd = (EditText) findViewById(R.id.pwdInStd);
        done = (Button) findViewById(R.id.doneStd);

        fbauthh = FirebaseAuth.getInstance();


        auth_listner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){

                     db = FirebaseDatabase.getInstance().getReference("User");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                DatabaseReference uniquee = db.child(fbauthh.getCurrentUser().getUid()).child("unique");
                                uniquee.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String unique = dataSnapshot.getValue(String.class);
                                        if (unique != null) {
                                            if (unique.equalsIgnoreCase("student")) {
                                                activeUserss.child(fbauthh.getCurrentUser().getUid()).setValue(fbauthh.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                           // Toast.makeText(stdIn.this, "User is active now", Toast.LENGTH_SHORT).show();
                                                            Intent nxt = new Intent(getApplicationContext(), stdMenu.class);
                                                            nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(nxt);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(stdIn.this, "User not active", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        };


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSign_in();

            }
        });




    }@Override
    protected void onStart() {
        super.onStart();

        fbauthh.addAuthStateListener(auth_listner);
    }




    private void startSign_in(){

        String e_mail = mail.getText().toString();
        String p_word = pwd.getText().toString();

        if (TextUtils.isEmpty(e_mail) || TextUtils.isEmpty(p_word)){

            Toast.makeText(this, "Data required!", Toast.LENGTH_SHORT).show();
        }


else{

          //  mProgress2.setTitle("Loading Data..!");
          //  mProgress2.setCanceledOnTouchOutside(false);
          //  mProgress2.show();
        fbauthh.signInWithEmailAndPassword(e_mail, p_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    db = FirebaseDatabase.getInstance().getReference("User");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                DatabaseReference uniquee = db.child(fbauthh.getCurrentUser().getUid()).child("unique");
                                uniquee.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String unique = dataSnapshot.getValue(String.class);
                                        if (unique != null) {

                                            if (unique.equalsIgnoreCase("student")) {
                                                activeUserss.child(fbauthh.getCurrentUser().getUid()).setValue(fbauthh.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                         //   Toast.makeText(stdIn.this, "User is active now", Toast.LENGTH_SHORT).show();
                                                            //mProgress2.dismiss();
                                                            Intent nxt = new Intent(getApplicationContext(), stdMenu.class);
                                                            nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(nxt);
                                                            finish();
                                                        } else {
                                                           // Toast.makeText(stdIn.this, "User not active", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(stdIn.this, "SignIn Error", Toast.LENGTH_SHORT).show();
                  //  mProgress2.dismiss();
                }
            }
        });
}
    }
}
