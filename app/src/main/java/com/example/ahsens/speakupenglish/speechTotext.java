package com.example.ahsens.speakupenglish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class speechTotext extends AppCompatActivity {


    EditText adtxt;
    Button publish;
    Spinner clas;
    FirebaseAuth fbAuth;
    DatabaseReference db;
    EditText adName;
    String paragraph;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_totext);

        getSupportActionBar().setTitle("Publish your own");


        adtxt = (EditText) findViewById(R.id.adtxt);
        adName = (EditText) findViewById(R.id.adName);
        publish = (Button) findViewById(R.id.publish);
        clas = (Spinner) findViewById(R.id.clas);


        fbAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Paragraph");

        paragraph = adtxt.getText().toString();


        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            Paragraphs pera = new Paragraphs();
            pera.setName(adName.getText().toString());
            pera.setValue(adtxt.getText().toString());
            db.child(clas.getSelectedItem().toString()).child(adName.getText().toString()).setValue(pera).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Intent i = new Intent(getApplicationContext(), stdMenu.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                        Toast.makeText(speechTotext.this, "Published Successfully..!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(speechTotext.this, "Error while uploading..!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });


        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(speechTotext.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Clas));

        myAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        clas.setAdapter(myAdapter2);






   }
}
