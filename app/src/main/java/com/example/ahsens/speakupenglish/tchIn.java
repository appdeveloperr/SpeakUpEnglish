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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class tchIn extends AppCompatActivity {

    EditText name, mail, pwd, qual;
    Button done;
DatabaseReference user,activeTech;
    FirebaseAuth fbauth;
    FirebaseAuth.AuthStateListener auth_listner;
    String qualification;
  //  ProgressDialog mProgress4,mProgress5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_in);

        getSupportActionBar().setTitle("Signin as Teacher");

      //  mProgress4 = new ProgressDialog(this);
      //  mProgress5 = new ProgressDialog(this);

        name = (EditText) findViewById(R.id.nameInTch);
        mail = (EditText) findViewById(R.id.mailInTch);
        pwd = (EditText) findViewById(R.id.pwdInTch);
        qual = (EditText) findViewById(R.id.qualTch);
        done = (Button) findViewById(R.id.doneTch);

        fbauth = FirebaseAuth.getInstance();

        user = FirebaseDatabase.getInstance().getReference("User");
        activeTech = FirebaseDatabase.getInstance().getReference("ActiveUser").child("Teacher");




        auth_listner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){
                    DatabaseReference quali = user.child(fbauth.getCurrentUser().getUid()).child("qualification");
                    quali.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            activeTech.child(fbauth.getCurrentUser().getUid()).setValue(fbauth.getCurrentUser().getEmail());
                            String qualificationn = dataSnapshot.getValue(String.class);
                            Intent nxt = new Intent(getApplicationContext(), tchMenu.class);
                            nxt.putExtra("quali",qualificationn);
                            nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(nxt);
                            finish();
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        fbauth.addAuthStateListener(auth_listner);
    }




    private void startSign_in(){

        String e_mail = mail.getText().toString();
        String p_word = pwd.getText().toString();
        String qualif = qual.getText().toString();

        if (TextUtils.isEmpty(e_mail) || TextUtils.isEmpty(p_word) || TextUtils.isEmpty(qualif)){

            Toast.makeText(this, "Data required!", Toast.LENGTH_SHORT).show();
        }
        else {

          //  mProgress5.setTitle("Loading Data..!");
          //  mProgress5.setCanceledOnTouchOutside(false);
          //  mProgress5.show();
            fbauth.signInWithEmailAndPassword(e_mail, p_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    final String uid = snapshot.getKey();

                                    if (uid.equalsIgnoreCase(fbauth.getCurrentUser().getUid())) {

                                        DatabaseReference unique = user.child(uid).child("unique");
                                        unique.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String uniq = dataSnapshot.getValue().toString();

                                                if (uniq.equalsIgnoreCase("Teacher")) {

                                                    DatabaseReference quali = user.child(uid).child("qualification");
                                                    quali.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            qualification = dataSnapshot.getValue().toString();
                                                            activeTech.child(fbauth.getCurrentUser().getUid()).setValue(fbauth.getCurrentUser().getEmail());
                                                            Intent nxt = new Intent(getApplicationContext(), tchMenu.class);
                                                            nxt.putExtra("quali", qualification);
                                                            nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(nxt);
                                                            finish();
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

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }else{

                        Toast.makeText(tchIn.this, "Error signing-in...!", Toast.LENGTH_SHORT).show();
                       // mProgress5.dismiss();
                    }



                }
            });
        }
    }
}
