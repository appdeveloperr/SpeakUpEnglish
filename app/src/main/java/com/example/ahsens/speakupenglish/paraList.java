package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class paraList extends AppCompatActivity {


    Button easy, med, hard, news, mag, my, book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_list);

        getSupportActionBar().setTitle("Get the level!");

        easy = (Button) findViewById(R.id.easy);
        med = (Button) findViewById(R.id.inter);
        hard = (Button) findViewById(R.id.hard);
        news = (Button) findViewById(R.id.npaper);
        mag = (Button) findViewById(R.id.mag);
        book = (Button) findViewById(R.id.books);
    //    my = (Button) findViewById(R.id.published);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Easy.class);
                i.putExtra("pEasy","pEasy");
                startActivity(i);
                Toast.makeText(paraList.this, "Easy List", Toast.LENGTH_SHORT).show();

            }
        });


        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Inter.class);
                i.putExtra("pInter","pInter");
                startActivity(i);
                Toast.makeText(paraList.this, "Intermediate List", Toast.LENGTH_SHORT).show();
            }
        });


        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Hard.class);
                i.putExtra("pHard","pHard");
                startActivity(i);
                Toast.makeText(paraList.this, "Hard List", Toast.LENGTH_SHORT).show();

            }
        });


        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), News.class);
                startActivity(i);
                Toast.makeText(paraList.this, "Newspaper List", Toast.LENGTH_SHORT).show();
            }
        });


        mag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Mag.class);
                startActivity(i);
                Toast.makeText(paraList.this, "Magezine List", Toast.LENGTH_SHORT).show();

            }
        });


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Books.class);
                startActivity(i);
                Toast.makeText(paraList.this, "Books List", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
