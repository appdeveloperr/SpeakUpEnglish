package com.example.ahsens.speakupenglish;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class AiResultList extends AppCompatActivity {

    ListView listView;
    DatabaseReference uResults;
    FirebaseAuth uAuth;
    ArrayList<AIResult> results = new ArrayList<>();
    ArrayList<String> resultsName = new ArrayList<String>();
    AIResult result;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_result_list);




        listView = (ListView) findViewById(R.id.AIresList);

        uAuth = FirebaseAuth.getInstance();
        uResults = FirebaseDatabase.getInstance().getReference("AIResult").child(getIntent().getStringExtra("uid"));

        uResults.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    result = snapshot.getValue(AIResult.class);
                    results.add(result);
                    resultsName.add(result.getPeraname());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

                    AIResult aiResult = results.get(position);

                    Intent goAIRes = new Intent(getApplicationContext(),AIResultCard.class);
                    goAIRes.putExtra("aiMarks",aiResult.getMarks());
                    goAIRes.putExtra("aiorgtxt",aiResult.getOrgtxt());
                    goAIRes.putExtra("airectex",aiResult.getRectxt());
                    goAIRes.putExtra("aipName",aiResult.getPeraname());
                    goAIRes.putExtra("aiCword",aiResult.getCorrectword());
                    goAIRes.putExtra("aiWword",aiResult.getWrongword());
                    startActivity(goAIRes);


            }
        });

    }
}
