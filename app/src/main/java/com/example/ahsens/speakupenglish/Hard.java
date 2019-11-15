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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Hard extends AppCompatActivity {

    TextView title, desc;
    Button ok;
    FirebaseAuth fbAuth;
    DatabaseReference db, db2;
    ListView listView;
    ArrayList<Paragraphs> arrayList = new ArrayList<>();
    ArrayList<String> nameofPera    = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Paragraphs pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard);

        getSupportActionBar().setTitle("Hard List");

        listView = (ListView) findViewById(R.id.hardList);


        db = FirebaseDatabase.getInstance().getReference("Paragraph").child("Hard");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    pr = snapshot.getValue(Paragraphs.class);
                    arrayList.add(pr);
                    nameofPera.add(pr.getName());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, nameofPera) {
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

                Paragraphs P = arrayList.get(position);
                Intent showPera = new Intent(getApplicationContext(),showParagraph.class);
                showPera.putExtra("pName",P.getName());
                showPera.putExtra("pValue",P.getValue());
                showPera.putExtra("pType",getIntent().getStringExtra("pHard"));
                startActivity(showPera);

            }
        });


    }

}
