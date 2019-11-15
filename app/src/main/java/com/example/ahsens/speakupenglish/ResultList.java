package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ResultList extends AppCompatActivity {

    ListView listView;
    DatabaseReference uResults;
    FirebaseAuth uAuth;
    ArrayList<sendResult> results = new ArrayList<>();
    ArrayList<String> resultsName = new ArrayList<String>();
    sendResult result;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        listView = (ListView) findViewById(R.id.resList);

        uAuth = FirebaseAuth.getInstance();
        uResults = FirebaseDatabase.getInstance().getReference("Result").child(getIntent().getStringExtra("uid"));

        uResults.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    result = snapshot.getValue(sendResult.class);
                    results.add(result);
                    resultsName.add(result.getUpname());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ResultList.this, "size of results: "+resultsName.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ResultList.this, "cncl ", Toast.LENGTH_SHORT).show();

            }
        });


        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, resultsName) {
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

                sendResult P = results.get(position);

                Intent goResult = new Intent(getApplicationContext(),Result.class);
                goResult.putExtra("uMarks",P.getMarks());
                goResult.putExtra("orgtxt",P.getTxtorg());
                goResult.putExtra("uTxt",P.getTxtrc());
                goResult.putExtra("tEmail",P.getTeamil());
                goResult.putExtra("uRec",P.getUaudio());
                goResult.putExtra("tRec",P.getTaudio());
                goResult.putExtra("pName",P.getUpname());
                startActivity(goResult);
            }
        });




    }
}
