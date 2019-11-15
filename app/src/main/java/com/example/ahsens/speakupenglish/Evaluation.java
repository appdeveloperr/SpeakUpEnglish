package com.example.ahsens.speakupenglish;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Evaluation extends AppCompatActivity {

    EditText marks;
    TextView uName, pName, pOrg, uData;
    Button post,uAudio,tAudio,stAudio;
    Task<Void> result;
    MediaPlayer player;
    String qualificatio;
    FirebaseAuth mAuth;
    String mFileName;
    MediaRecorder mmRecorder;
    sendResult sendResult;
    StorageReference mStorage;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        marks = (EditText) findViewById(R.id.uMarks);
        uName = (TextView) findViewById(R.id.uName);
        pName = (TextView) findViewById(R.id.pName);
        pOrg = (TextView) findViewById(R.id.pOrg);
        uData = (TextView) findViewById(R.id.uData);
        post = (Button) findViewById(R.id.postRes);
        uAudio = (Button)findViewById(R.id.uAudio);
        tAudio = (Button)findViewById(R.id.TAudio);
        stAudio = (Button)findViewById(R.id.sTAudio);

        manageBtnClick();
        mAuth = FirebaseAuth.getInstance();

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/Recorded_audio.mp3";
        mStorage = FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid());


        uName.setText(getIntent().getStringExtra("uName"));
        pName.setText(getIntent().getStringExtra("pName"));
        pOrg.setText(getIntent().getStringExtra("pOrg"));
        uData.setText(getIntent().getStringExtra("uData"));

        qualificatio = getIntent().getStringExtra("quali");



        tAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRecording();
                stAudio.setVisibility(View.VISIBLE);
               tAudio.setVisibility(View.INVISIBLE);
            }
        });

        stAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopRecording();
                tAudio.setVisibility(View.VISIBLE);
                stAudio.setVisibility(View.INVISIBLE);


            }
        });







        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {

            player.setDataSource(getIntent().getStringExtra("uAudio"));
            player.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }



        uAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(marks.getText().toString()!=null) {
                    UploadAudio();
                }else{
                    Toast.makeText(Evaluation.this, "Marks Required..!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void DeleteCheckedPera() {
        DatabaseReference  peraType = FirebaseDatabase.getInstance().getReference("checkPera");

        if(qualificatio!=null) {
            if (qualificatio.equalsIgnoreCase("BS")) {

                final DatabaseReference pEasy = peraType.child("pEasy");
                pEasy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final String uid = snapshot.getKey();
                            DatabaseReference getChkPeraData = pEasy.child(uid).child(getIntent().getStringExtra("pName"));
                            getChkPeraData.removeValue();
                            Intent goTmenu = new Intent(getApplicationContext(),tchMenu.class);
                            goTmenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goTmenu);
                            finish();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else if (qualificatio.equalsIgnoreCase("MS")) {
                final DatabaseReference pInter = peraType.child("pInter");
                pInter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final String uid = snapshot.getKey();
                            DatabaseReference getChkPeraData = pInter.child(uid).child(getIntent().getStringExtra("pName"));
                            getChkPeraData.removeValue();
                            Intent goTmenu = new Intent(getApplicationContext(),tchMenu.class);
                            goTmenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           goTmenu.putExtra("quali",qualificatio);
                            startActivity(goTmenu);
                            finish();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else if (qualificatio.equalsIgnoreCase("Phd")) {
                final DatabaseReference pHard = peraType.child("pHard");
                pHard.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final String uid = snapshot.getKey();
                            DatabaseReference getChkPeraData = pHard.child(uid).child(getIntent().getStringExtra("pName"));;
                            getChkPeraData.removeValue();
                            Intent goTmenu = new Intent(getApplicationContext(),tchMenu.class);
                            goTmenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goTmenu);
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }

    }

    private void startRecording() {

        mmRecorder = new MediaRecorder();
        mmRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mmRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mmRecorder.setOutputFile(mFileName);
        mmRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mmRecorder.prepare();
        } catch (IOException e) {
        }

        mmRecorder.start();
    }


    private void stopRecording() {
        mmRecorder.stop();
        mmRecorder.release();
        mmRecorder = null;

        UploadAudio();
    }


    private void UploadAudio(){


       StorageReference filepath = mStorage.child(getIntent().getStringExtra("pName"));

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("mp3")
                .build();

        Uri audiofile = Uri.fromFile(new File(mFileName));

        filepath.putFile(audiofile,metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

             String downurl = taskSnapshot.getDownloadUrl().toString();
                sendResult = new sendResult();
                sendResult.setMarks(marks.getText().toString());
                sendResult.setTxtorg(getIntent().getStringExtra("pOrg"));
                sendResult.setTxtrc(getIntent().getStringExtra("uData"));
                sendResult.setTeamil(mAuth.getCurrentUser().getEmail());
                sendResult.setTaudio(downurl);
                sendResult.setUaudio(getIntent().getStringExtra("uAudio"));
                sendResult.setUpname(getIntent().getStringExtra("pName"));

                result = FirebaseDatabase.getInstance().getReference("Result").child(getIntent().getStringExtra("uId"))
                        .child(getIntent().getStringExtra("pName")).setValue(sendResult).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Evaluation.this, "Result Uploaded..!", Toast.LENGTH_SHORT).show();
                                    DeleteCheckedPera();
                                }else{
                                    Toast.makeText(Evaluation.this, "Result Not Uploaded..!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });

    }



    private void manageBtnClick() {

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String [] {android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){


            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO:
                if(grantResults .length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                }

                else{

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)){

                        new AlertDialog.Builder(this).setTitle("Read Contacts permission").
                                setMessage("You need to grant audio record permission to use audio"+
                                        "recorder feature. Retry and grant it..!").show();
                    }
                    else{

                        new AlertDialog.Builder(this).setTitle("Read Contacts permission denied").
                                setMessage("You denied audio record permission. So, the feature will be disabled." +
                                        "To Enable it, go on settings and grant audio record for the application");
                    }


                }

                break;

        }


    }



}
