package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class showParagraph extends AppCompatActivity {

    TextView name, val;
    Button okPera;
    String peraType,alphaOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_paragraph);

        okPera =(Button)findViewById(R.id.okPera);
        name = (TextView) findViewById(R.id.pName);
        val = (TextView) findViewById(R.id.pValue);




        alphaOnly = getIntent().getStringExtra("pValue").replaceAll("[-+.,^%:;]","");

        name.setText(getIntent().getStringExtra("pName"));
        val.setText(alphaOnly);


        if(getIntent().getStringExtra("pType")!=null){

            peraType = getIntent().getStringExtra("pType");
        }



        okPera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoTrial = new Intent(getApplicationContext(),Trial.class);
                gotoTrial.putExtra("pName",getIntent().getStringExtra("pName"));
                gotoTrial.putExtra("pValue",alphaOnly);
                gotoTrial.putExtra("pType",peraType);
                startActivity(gotoTrial);

            }
        });


    }
}
