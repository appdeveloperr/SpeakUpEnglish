package com.example.ahsens.speakupenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class tchText extends AppCompatActivity {

    TextView tchCor;
    Button tchDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_text);

        tchCor = (TextView) findViewById(R.id.tchCor);
        tchDone = (Button) findViewById(R.id.tchDone);

    }
}
