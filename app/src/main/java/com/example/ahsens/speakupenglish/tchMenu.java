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
import java.util.List;

public class tchMenu extends AppCompatActivity {

    Button tchout;
    FirebaseAuth ffbauth;
    ListView stuList;
    DatabaseReference peraType;
    ArrayList<String>uids = new ArrayList<>();
    ArrayList<String> checkPDname = new ArrayList<>();
    ArrayList<checkperaData> checkperaDataArr = new ArrayList<>();
    ArrayAdapter adapter;
    String qualificatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_menu);

        getSupportActionBar().setTitle("Teacher's Menu");


        tchout  = (Button)findViewById(R.id.tchOut);
        stuList = (ListView)findViewById(R.id.stuList);

        Toast.makeText(this, "teacher Qualification: "+getIntent().getStringExtra("quali"), Toast.LENGTH_SHORT).show();


         qualificatio = getIntent().getStringExtra("quali");

        peraType = FirebaseDatabase.getInstance().getReference("checkPera");

        if(qualificatio!=null) {
            if (qualificatio.equalsIgnoreCase("BS")) {

                final DatabaseReference pEasy = peraType.child("pEasy");
                pEasy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final String uid = snapshot.getKey();
                            DatabaseReference getChkPeraData = pEasy.child(uid);
                            getChkPeraData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postsnap : dataSnapshot.getChildren()) {

                                        checkperaData CPD = postsnap.getValue(checkperaData.class);
                                        checkperaDataArr.add(CPD);
                                        checkPDname.add(CPD.getStuname());
                                        uids.add(uid);
                                        adapter.notifyDataSetChanged();
                                    }
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

            } else if (qualificatio.equalsIgnoreCase("MS")) {
                final DatabaseReference pInter = peraType.child("pInter");
                pInter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final String uid = snapshot.getKey();
                            DatabaseReference getChkPeraData = pInter.child(uid);
                            getChkPeraData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postsnap : dataSnapshot.getChildren()) {

                                        checkperaData CPD = postsnap.getValue(checkperaData.class);
                                        checkperaDataArr.add(CPD);
                                        checkPDname.add(CPD.getStuname());
                                        uids.add(uid);
                                        adapter.notifyDataSetChanged();
                                    }
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

            } else if (qualificatio.equalsIgnoreCase("Phd")) {
                final DatabaseReference pHard = peraType.child("pHard");
                pHard.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final String uid = snapshot.getKey();
                            DatabaseReference getChkPeraData = pHard.child(uid);
                            getChkPeraData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postsnap : dataSnapshot.getChildren()) {

                                        checkperaData CPD = postsnap.getValue(checkperaData.class);
                                        checkperaDataArr.add(CPD);
                                        checkPDname.add(CPD.getStuname());
                                        uids.add(uid);
                                        adapter.notifyDataSetChanged();
                                    }
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

            }
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, checkPDname) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.parseColor("#0039cb"));

                return view;
            }
        };
        //arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nameofPera);
        stuList.setAdapter(adapter);

        stuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checkperaData CperaData = checkperaDataArr.get(position);
                String userId = uids.get(position);

                Intent next = new Intent(getApplicationContext(), Evaluation.class);
                next.putExtra("quali",qualificatio);
                next.putExtra("uName", CperaData.getStuname());
                next.putExtra("pName", CperaData.getPeraname());
                next.putExtra("pOrg", CperaData.getPeraorg());
                next.putExtra("uData", CperaData.getPerarc());
                next.putExtra("uAudio",CperaData.getAudiourl());
                next.putExtra("uId", userId);
                startActivity(next);


            }
        });






        ffbauth = FirebaseAuth.getInstance();





        tchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference activeUserss= FirebaseDatabase.getInstance().getReference("ActiveUser").child("Teacher").child(ffbauth.getCurrentUser().getUid());
                activeUserss.removeValue();
                ffbauth.signOut();
                Toast.makeText(getApplicationContext(), "Signing-out...", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
