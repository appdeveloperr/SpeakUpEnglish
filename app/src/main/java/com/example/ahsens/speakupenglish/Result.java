package com.example.ahsens.speakupenglish;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;

public class Result extends AppCompatActivity {

    TextView grade, perc,checkedT;
    TextView org;
    TextView your;
    TextView marks;
    Button tRec,yourR;
    DatabaseReference getResult;
    FirebaseAuth mAuth;
    double perr;
    sendResult result;
    MediaPlayer player;
    DecimalFormat f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setTitle("Results");

        grade = (TextView) findViewById(R.id.grade);
        perc = (TextView) findViewById(R.id.perc);
        marks = (TextView) findViewById(R.id.marks);
        org = (TextView) findViewById(R.id.org);
        your = (TextView) findViewById(R.id.your);
        checkedT = (TextView)findViewById(R.id.checkedT);
        yourR = (Button)findViewById(R.id.yourR);
        tRec = (Button)findViewById(R.id.tRec);


        Toast.makeText(this, "Pera Name: "+getIntent().getStringExtra("pName"), Toast.LENGTH_SHORT).show();

        org.setText(getIntent().getStringExtra("orgtxt"));
        marks.setText(getIntent().getStringExtra("uMarks"));
        your.setText(getIntent().getStringExtra("uTxt"));
        checkedT.setText(getIntent().getStringExtra("tEmail"));
        checkGrade(getIntent().getStringExtra("uMarks"));
        checkPerc(getIntent().getStringExtra("uMarks"));



        mAuth = FirebaseAuth.getInstance();
        getResult = FirebaseDatabase.getInstance().getReference("Result").child(mAuth.getCurrentUser().getUid());


        getResult.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {

                    result = postsnap.getValue(sendResult.class);
                    org.setText(result.getTxtorg());
                    marks.setText(result.getMarks());
                    your.setText(result.getTxtrc());
                    checkedT.setText(result.getTeamil());
                    checkGrade(result.getMarks());
                    checkPerc(result.getMarks());
                    //perc.setText();


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);


        tRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (result.getTaudio() != null){

                    try {
                        Toast.makeText(Result.this, "Teacher's Audio Starting", Toast.LENGTH_SHORT).show();
                        player.reset();
                        player.setDataSource(getIntent().getStringExtra("tRec"));
                        // player.setDataSource(result.getTaudio());
                        player.prepare();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                player.start();
            }



            }
        });

        yourR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (result.getUaudio() != null) {


                    try {
                        Toast.makeText(Result.this, "Student Audio Starting", Toast.LENGTH_SHORT).show();
                        player.reset();
                        player.setDataSource(getIntent().getStringExtra("uRec"));
                        player.prepare();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    player.start();
                }


            }
        });










    }

    private void checkGrade(String marks) {

        if(marks !=null) {

            double Marksgrade = Double.parseDouble(marks);

            if (Marksgrade >= 90) {

                grade.setText("A+");
            } else if (Marksgrade >= 80 && Marksgrade < 90) {

                grade.setText("A");
            } else if (Marksgrade >= 70 && Marksgrade < 80) {

                grade.setText("B");
            } else if (Marksgrade >= 60 && Marksgrade < 70) {

                grade.setText("C");
            } else if (Marksgrade >= 50 && Marksgrade < 60) {

                grade.setText("D");
            } else {

                grade.setText("F");
            }
        }

    }

    private void checkPerc(String marks) {

            double per = Double.parseDouble(marks);
            perr = (per / 100) * 100;
            f = new DecimalFormat("##.00");
            perc.setText(f.format(perr)+"%");
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        player.stop();
    }
}

