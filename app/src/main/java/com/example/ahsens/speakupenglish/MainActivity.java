package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button sIn, sUp, exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sIn = (Button) findViewById(R.id.signin);
        sUp = (Button) findViewById(R.id.signup);
        exit = (Button) findViewById(R.id.exit);


        sIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, sIn);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.std) {

                            Intent i = new Intent(getApplicationContext(), stdIn.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Singing-in as: " + menuItem, Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (menuItem.getItemId() == R.id.tch) {

                            Intent i = new Intent(getApplicationContext(), tchIn.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Singing-in as: " + menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        } else if (menuItem.getItemId() == R.id.adm) {

                            Intent i = new Intent(getApplicationContext(), admIn.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Singing-in as: " + menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        return true;
                    }
                });

                popupMenu.show();

            }

        });



        sUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, sUp);
                popupMenu.getMenuInflater().inflate(R.menu.sup, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.std) {

                            Intent i = new Intent(getApplicationContext(), stdForm.class);
                            i.putExtra("fchoice",menuItem.toString());
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Singing-up as: " + menuItem, Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        else if (menuItem.getItemId() == R.id.tch) {

                            Intent i = new Intent(getApplicationContext(), tchForm.class);
                            i.putExtra("fchoice",menuItem.toString());
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Singing-up as: " + menuItem, Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        return true;
                    }
                });

                popupMenu.show();

            }

        });



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                System.exit(0);
            }
        });

    }
}
