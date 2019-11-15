package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class evAI extends AppCompatActivity {

    TextView orgP, myP, aiRes, peraNam, wrong, correctt;
    Button ok;
    int count=0;
    double total_marks,finalMarks;
    DecimalFormat f;
    String corWords,wrongWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_ai);

        ok = (Button) findViewById(R.id.ok);
        aiRes = (TextView) findViewById(R.id.aiRes);
        orgP = (TextView) findViewById(R.id.org);
        myP = (TextView) findViewById(R.id.yours);
        wrong = (TextView) findViewById(R.id.wrong);
        correctt = (TextView) findViewById(R.id.cor);
        peraNam = (TextView) findViewById(R.id.perNam);


        myP.setText(getIntent().getStringExtra("recPera"));
        peraNam.setText(getIntent().getStringExtra("pName"));
        orgP.setText(getIntent().getStringExtra("pValue"));


        checkingByAI();



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), stdMenu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });



    }

    private void checkingByAI() {


        String orgtxt = getIntent().getStringExtra("pValue");
        String utxt   = getIntent().getStringExtra("recPera");


        String[] items1 = orgtxt.split(" ");
        String[] items2 = utxt.split(" ");

        ArrayList arr1 = new ArrayList();
        ArrayList arr2 = new ArrayList();

        ArrayList correct = new ArrayList();
        ArrayList mistakes = new ArrayList();


        for(int i = 0 ; i < items1.length;i++){

            arr1.add(items1[i]);

        }

        for(int i = 0 ; i < items2.length;i++){

            arr2.add(items2[i]);
        }


        Toast.makeText(this, "size of arr1: "+arr1.size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "size of arr2: "+arr2.size(), Toast.LENGTH_SHORT).show();



        for(int j = 0 ; j <arr2.size();j++){


            String uword = arr2.get(j).toString();
            String pword = arr1.get(j).toString();

            if(!uword.equalsIgnoreCase(pword))
            {
                count++;
                correct.add(pword+", ");
                mistakes.add(uword+", ");

            }
        }


         corWords = correct.toString().replaceAll("[\\[\\](){}]","");
         wrongWord = mistakes.toString().replaceAll("[\\[\\](){}]","");
        correctt.setText(corWords);
        wrong.setText(wrongWord);

        String P1 = TextUtils.join(" ", arr1);
        String P2 = TextUtils.join(" ", arr2);
        orgP.setText(P1);
        myP.setText(P2);

        //Creating Result
        total_marks = 100;
        finalMarks = total_marks -count;
        Toast.makeText(this, "final marks: "+finalMarks, Toast.LENGTH_SHORT).show();
        checkGrade(Double.toString(finalMarks));


    }



    private void checkGrade(String marks) {

        if(marks !=null) {

            double Marksgrade = Double.parseDouble(marks);

            if (Marksgrade >= 90) {

                aiRes.setText("A+");
            } else if (Marksgrade >= 80 && Marksgrade < 90) {

                aiRes.setText("A");
            } else if (Marksgrade >= 70 && Marksgrade < 80) {

                aiRes.setText("B");
            } else if (Marksgrade >= 60 && Marksgrade < 70) {

                aiRes.setText("C");
            } else if (Marksgrade >= 50 && Marksgrade < 60) {

                aiRes.setText("D");
            } else {

                aiRes.setText("F");
            }

            checkPerc(marks);
        }


    }

    private void checkPerc(String marks) {

        double per = Double.parseDouble(marks);
        double perr = (per / 100) * 100;
        f = new DecimalFormat("##.00");
        aiRes.setText("Percentage: "+f.format(perr)+"%"+"\tGrade: "+aiRes.getText().toString());

        uploadAIresult();
    }



    private void uploadAIresult() {

                AIResult aiResult = new AIResult();
                aiResult.setMarks(Double.toString(finalMarks));
                aiResult.setOrgtxt(getIntent().getStringExtra("pValue"));
                aiResult.setRectxt(getIntent().getStringExtra("recPera"));
                aiResult.setPeraname(getIntent().getStringExtra("pName"));
                aiResult.setCorrectword(corWords);
                aiResult.setWrongword(wrongWord);


            DatabaseReference aIResult = FirebaseDatabase.getInstance().getReference("AIResult")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getIntent().getStringExtra("pName"));

            aIResult.setValue(aiResult).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(evAI.this, "Result Uploaded by AI..!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(evAI.this, "Error while uploading result..!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


}
