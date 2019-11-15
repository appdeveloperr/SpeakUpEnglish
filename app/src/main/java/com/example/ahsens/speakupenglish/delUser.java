package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class delUser extends AppCompatActivity {

    DatabaseReference checkActive,user;
    ArrayList<String> nameofStd = new ArrayList<>();
    ArrayList<String> userID = new ArrayList<>();
    ArrayAdapter<String>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_user);


        ListView listView = (ListView) findViewById(R.id.delList);
        TextView tvv = (TextView)findViewById(R.id.tvD);

        user = FirebaseDatabase.getInstance().getReference("User");

        if(getIntent().getStringExtra("lType").equalsIgnoreCase("S")){
            checkActive = FirebaseDatabase.getInstance().getReference("ActiveUser").child("Student");
            tvv.setText("Students");
        }else if(getIntent().getStringExtra("lType").equalsIgnoreCase("T"))
        {
            checkActive = FirebaseDatabase.getInstance().getReference("ActiveUser").child("Teacher");
            tvv.setText("Teachers");
        }

        checkActive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    String uid = snapshot.getKey();
                    userID.add(uid);
                    DatabaseReference email  = checkActive.child(uid);
                    email.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String uemail = dataSnapshot.getValue(String.class);
                            nameofStd.add(uemail);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, nameofStd) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.parseColor("#0039cb"));
                return view;
            }
        };

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uid = userID.get(position);
             checkActive.child(uid).removeValue();
                user.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(delUser.this, "User Deleted", Toast.LENGTH_SHORT).show();
                            Intent goback = new Intent(getApplicationContext(),admPanel.class);
                            goback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goback);
                            finish();

                        }else
                        {
                            Toast.makeText(delUser.this, "User not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }

}

