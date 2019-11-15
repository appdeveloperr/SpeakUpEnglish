package com.example.ahsens.speakupenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class AIResultCard extends AppCompatActivity {

    TextView orgPai, myPai, aiResai, peraNamai, wrongai, correcttai;
    DecimalFormat f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airesult_card);

        aiResai = (TextView) findViewById(R.id.aiResai);
        orgPai = (TextView) findViewById(R.id.orgai);
        myPai = (TextView) findViewById(R.id.yoursai);
        wrongai = (TextView) findViewById(R.id.wrongai);
        correcttai = (TextView) findViewById(R.id.corai);
        peraNamai = (TextView) findViewById(R.id.perNamai);


        peraNamai.setText(getIntent().getStringExtra("aipName"));
        orgPai.setText(getIntent().getStringExtra("aiorgtxt"));
        myPai.setText(getIntent().getStringExtra("airectex"));
        wrongai.setText(getIntent().getStringExtra("aiWword"));
        correcttai.setText(getIntent().getStringExtra("aiCword"));
        checkGrade(getIntent().getStringExtra("aiMarks"));

    }


    private void checkGrade(String marks) {

        if(marks !=null) {

            double Marksgrade = Double.parseDouble(marks);

            if (Marksgrade >= 90) {

                aiResai.setText("A+");
            } else if (Marksgrade >= 80 && Marksgrade < 90) {

                aiResai.setText("A");
            } else if (Marksgrade >= 70 && Marksgrade < 80) {

                aiResai.setText("B");
            } else if (Marksgrade >= 60 && Marksgrade < 70) {

                aiResai.setText("C");
            } else if (Marksgrade >= 50 && Marksgrade < 60) {

                aiResai.setText("D");
            } else {

                aiResai.setText("F");
            }

            checkPerc(marks);
        }


    }

    private void checkPerc(String marks) {

        double per = Double.parseDouble(marks);
        double perr = (per / 100) * 100;
        f = new DecimalFormat("##.00");
        aiResai.setText("Percentage: "+f.format(perr)+"%"+"\tGrade: "+aiResai.getText().toString());

    }

}
