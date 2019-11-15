package com.example.ahsens.speakupenglish;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StuList extends AppCompatActivity {

    FirebaseAuth fbAuth;
    DatabaseReference checkActive, user;
    ListView listView;
    ArrayList<Paragraphs> studentList = new ArrayList<>();
    ArrayList<String> nameofStd    = new ArrayList<>();
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_list);

        listView = (ListView) findViewById(R.id.stList);
        TextView tvv = (TextView)findViewById(R.id.tvv);

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
    }

}
