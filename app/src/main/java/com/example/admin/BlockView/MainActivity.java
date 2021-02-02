package com.example.admin.BlockView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button Loginbutton;
    EditText mail, pass;
    String mailS, passS;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//progressBar=findViewById(R.id.bar);
        mail = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        Loginbutton = findViewById(R.id.login);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Logging In");
                progressDialog.show();

                mailS = mail.getText().toString();
                passS = pass.getText().toString();

                if (mailS.isEmpty()) {
                    mail.setError("Mail is required");
                    progressDialog.dismiss();
                    mail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mailS).matches()) {
                    mail.setError("Enter valid Email");
                    progressDialog.dismiss();
                    mail.requestFocus();
                    return;
                }
                if (passS.isEmpty()) {
                    pass.setError("Password is required");
                    progressDialog.dismiss();
                    pass.requestFocus();
                    return;
                }
                if (passS.length() < 5) {
                    pass.setError("Minimum length should be 5 ");
                    progressDialog.dismiss();
                    pass.requestFocus();
                    return;
                }

                auth.signInWithEmailAndPassword(mailS, passS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            progressDialog.dismiss();
                            startActivity(intent);

                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        });

    }

    ////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.mymenu, menu);
        return true;
    }


    //@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.ANU:
                //Toast.makeText(this,"code",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, ANU.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
