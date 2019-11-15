package com.example.ahsens.speakupenglish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class admIn extends AppCompatActivity {

    EditText mail, pwd;
    Button done;
    FirebaseAuth fffbauth;
    FirebaseAuth.AuthStateListener auth_listner;
    //ProgressDialog mProgress3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_in);

        mail = (EditText) findViewById(R.id.mailInAdm);
        pwd = (EditText) findViewById(R.id.pwdInAdm);
        done = (Button) findViewById(R.id.doneAdm);

//        mProgress3 = new ProgressDialog(this);
        fffbauth = FirebaseAuth.getInstance();

      /*  auth_listner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){

                    Intent nxt = new Intent(getApplicationContext(), admPanel.class);
                    nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(nxt);
                    finish();
                }
            }
        };*/

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSign_in();

            }
        });
    }



    private void startSign_in(){

        final String e_mail = mail.getText().toString();
        final String p_word = pwd.getText().toString();

        if (!TextUtils.isEmpty(e_mail) && !TextUtils.isEmpty(p_word)){


                fffbauth.signInWithEmailAndPassword(e_mail, p_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if(e_mail.equalsIgnoreCase("iadmin@speakup.com")&&
                                    p_word.equals("12345678")) {

                                //  mProgress3.dismiss();
                                Intent nxt = new Intent(getApplicationContext(), admPanel.class);
                                nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(nxt);
                                finish();
                            }
                        } else {
                            Toast.makeText(admIn.this, "SignIn Error..!", Toast.LENGTH_SHORT).show();
                            // mProgress3.dismiss();
                        }
                    }
                });

        }else
        {
            Toast.makeText(this, "Data required..!", Toast.LENGTH_SHORT).show();
        }


    }
}
