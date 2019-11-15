package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class stdMenu extends AppCompatActivity {

    Button  para, evaluate, results, select, stdOut;
    DatabaseReference user;
    FirebaseAuth fbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_menu);

        getSupportActionBar().setTitle("Student Menu");

        para =(Button) findViewById(R.id.para);
        select =(Button) findViewById(R.id.select);
        results =(Button) findViewById(R.id.results);
        stdOut =(Button) findViewById(R.id.stdOut);

        fbauth = FirebaseAuth.getInstance();



        para.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), speechTotext.class);
                startActivity(i);
                Toast.makeText(stdMenu.this, "Add paragraph", Toast.LENGTH_SHORT).show();


            }
        });






        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(stdMenu.this, select);
                popupMenu.getMenuInflater().inflate(R.menu.paramenu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.uk) {

                            Intent i = new Intent(getApplicationContext(), paraList.class);
                            startActivity(i);
                            Toast.makeText(stdMenu.this, "Selected origin: "+menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        else if (menuItem.getItemId() == R.id.us) {

                            Intent i = new Intent(getApplicationContext(), paraList.class);
                            startActivity(i);
                            Toast.makeText(stdMenu.this, "Selected origin: "+menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        return true;
                    }
                });

                popupMenu.show();

            }
        });


        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popupMenu = new PopupMenu(stdMenu.this, results);
                popupMenu.getMenuInflater().inflate(R.menu.testmenu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.ai) {

                            Intent i = new Intent(getApplicationContext(), AiResultList.class);
                            i.putExtra("uid",fbauth.getCurrentUser().getUid());
                            startActivity(i);
                            return true;

                        } else if (menuItem.getItemId() == R.id.tech) {

                            Intent i = new Intent(getApplicationContext(), ResultList.class);
                            i.putExtra("uid",fbauth.getCurrentUser().getUid());
                            startActivity(i);

                            return true;
                        }

                        return true;
                    }
                });

                popupMenu.show();


            }
        });


        stdOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference activeUsers= FirebaseDatabase.getInstance().getReference("ActiveUser").child("Student").child(fbauth.getCurrentUser().getUid());
                activeUsers.removeValue();
                fbauth.signOut();
                Toast.makeText(stdMenu.this, "Signing-out...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();

            }
        });
    }

}
