package com.example.ahsens.speakupenglish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.function.ToLongBiFunction;

public class admPanel extends AppCompatActivity {

    Button stdList,tchList, delUser, admOut;
    FirebaseAuth mAuth;
    DatabaseReference setlogout_admin;
    FirebaseAuth fbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_panel);

        getSupportActionBar().setTitle("Admin Panel");




        mAuth = FirebaseAuth.getInstance();
        setlogout_admin = FirebaseDatabase.getInstance().getReference("LoginStatus");

        stdList       = (Button)findViewById(R.id.stdList);
        tchList       = (Button)findViewById(R.id.tchList);
        delUser       = (Button)findViewById(R.id.delUser);
        admOut        = (Button)findViewById(R.id.admOut);


        fbauth = FirebaseAuth.getInstance();


        stdList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), StuList.class);
                i.putExtra("lType","S");
                Toast.makeText(admPanel.this, "Student's List", Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });


        tchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), StuList.class);
                Toast.makeText(admPanel.this, "Teacher's List", Toast.LENGTH_SHORT).show();
                i.putExtra("lType","T");
                startActivity(i);

            }
        });


        delUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(admPanel.this, delUser);
                popupMenu.getMenuInflater().inflate(R.menu.sup, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.std) {

                            Intent i = new Intent(getApplicationContext(), delUser.class);
                            i.putExtra("lType","S");
                            startActivity(i);
                            Toast.makeText(admPanel.this, "" +menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        else if (menuItem.getItemId() == R.id.tch) {

                            Intent i = new Intent(getApplicationContext(), delUser.class);
                            i.putExtra("lType","T");
                            startActivity(i);
                            Toast.makeText(admPanel.this, ""+menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        return true;
                    }
                });

                popupMenu.show();


            }
        });


        admOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(admPanel.this, "Signing-out...", Toast.LENGTH_SHORT).show();
                fbauth.signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });


    }

}


