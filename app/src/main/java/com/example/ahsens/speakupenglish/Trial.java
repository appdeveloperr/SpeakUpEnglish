package com.example.ahsens.speakupenglish;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.datatype.Duration;

public class Trial extends AppCompatActivity {


    TextView txtrecord, txtOrg, peraName;
    Button start,done, stop, sendPera,recVoice;
    FirebaseAuth fbAuth;
    DatabaseReference db;
    DatabaseReference sendToTeacher;
    ArrayList<String> matches = new ArrayList<>();
    Task<Void> peraOrg,peraRc,peraname,stuname;
    FirebaseAuth mAuth;
    MediaRecorder mRecorder;
    ProgressDialog Progressdialog;
    String mFileName = null;
    String downurl, n, t;
    StorageReference mStorage;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 2;
    StorageReference filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        getSupportActionBar().setTitle("Test your skills");

        txtrecord = (TextView) findViewById(R.id.txt);
        txtOrg = (TextView) findViewById(R.id.txtOrg);
        peraName = (TextView) findViewById(R.id.etxt);
        start = (Button) findViewById(R.id.strt);
        done = (Button) findViewById(R.id.done);
        stop = (Button) findViewById(R.id.stop);
        recVoice = (Button) findViewById(R.id.recVoice);
        sendPera = (Button)findViewById(R.id.sendPera);


        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/Recorded_audio.mp3";


        manageBtnClick();

        peraName.setText(getIntent().getStringExtra("pName"));
        txtOrg.setText(getIntent().getStringExtra("pValue"));



        mAuth = FirebaseAuth.getInstance();
        sendToTeacher = FirebaseDatabase.getInstance().getReference("checkPera");


        sendPera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Trial.this, sendPera);
                popupMenu.getMenuInflater().inflate(R.menu.testmenu, popupMenu.getMenu());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.ai) {

                            if(getIntent().getStringExtra("pValue").length() >= matches.get(0).length() ) {
                                Intent i = new Intent(getApplicationContext(), evAI.class);
                                i.putExtra("recPera", matches.get(0));
                                i.putExtra("pValue", getIntent().getStringExtra("pValue"));
                                i.putExtra("pName", getIntent().getStringExtra("pName"));
                                startActivity(i);
                                Toast.makeText(Trial.this, "Evaluation by: AI", Toast.LENGTH_SHORT).show();

                                return true;
                            }else{
                                Toast.makeText(Trial.this, "Length of Input text is greater than Paragraph length..!", Toast.LENGTH_SHORT).show();
                                return  true;
                            }

                        } else if (menuItem.getItemId() == R.id.tech) {

                            UploadAudio();
                            Toast.makeText(Trial.this, "Evaluation by: Teacher", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });



        stop.setVisibility(View.INVISIBLE);
        done.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);

        //checkPermission();
        fbAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Paragraph");
        mStorage = FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid());
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra
                (RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizerIntent.putExtra
                (RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        mSpeechRecognizerIntent.putExtra
                (RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 15000);
        mSpeechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_RESULTS, 0);


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReadyForSpeech(Bundle bundle) {


            }

            @Override
            public void onBeginningOfSpeech() {


            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                //displaying the first match(best)
                if (matches != null)
                   // db.child(fbAuth.getCurrentUser().getUid()).child(txtrecord.getText().toString()).setValue(matches);
                    txtrecord.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                txtrecord.setText("");
                txtrecord.setHint("Listening...");
                start.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSpeechRecognizer.stopListening();
                txtrecord.setHint("You will see input here");
                start.setVisibility(View.VISIBLE);
                done.setVisibility(View.INVISIBLE);
            }
        });


        recVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRecording();
                stop.setVisibility(View.VISIBLE);
                recVoice.setVisibility(View.INVISIBLE);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopRecording();
                recVoice.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);


            }
        });

    }

    private void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }

        mRecorder.start();
    }


    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        //UploadAudio();
    }


    private void UploadAudio(){


        filepath = mStorage.child(getIntent().getStringExtra("pName"));

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("mp3")
                .build();

        Uri audiofile = Uri.fromFile(new File(mFileName));

        filepath.putFile(audiofile,metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                   downurl = taskSnapshot.getDownloadUrl().toString();
                Toast.makeText(Trial.this, "Audio Successfully Uploaded..!", Toast.LENGTH_SHORT).show();
                checkperaData checkperaData  = new checkperaData();
                checkperaData.setPeraorg(getIntent().getStringExtra("pValue"));
                checkperaData.setPerarc(matches.get(0));
                checkperaData.setPeraname(getIntent().getStringExtra("pName"));
                checkperaData.setStuname(mAuth.getCurrentUser().getEmail());
                checkperaData.setAudiourl(taskSnapshot.getDownloadUrl().toString());
                peraOrg = sendToTeacher.child(getIntent().getStringExtra("pType")).child(mAuth.getCurrentUser()
                        .getUid()).child(getIntent().getStringExtra("pName")).setValue(checkperaData);



            }
        });

    }








//Recording Permission Code



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
