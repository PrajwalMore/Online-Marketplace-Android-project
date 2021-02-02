package com.example.admin.BlockView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ANU extends AppCompatActivity {
Button button;
EditText name,number,mail,add,pass;
DatabaseReference ref;
ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anu);
        name=findViewById(R.id.name);
        number=findViewById(R.id.num);
        mail=findViewById(R.id.mid);
        add=findViewById(R.id.add);
        pass=findViewById(R.id.pass1);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

    }


    private void Register() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering User");



        String nameS,numberS,mailS,addS,passS;
        nameS=name.getText().toString().trim();
        numberS=number.getText().toString().trim();
        mailS=mail.getText().toString().trim();
        addS=add.getText().toString().trim();
        passS=pass.getText().toString().trim();

        if(nameS.isEmpty()){
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
        if(numberS.isEmpty()) {
            number.setError("number is required");
            number.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(numberS).matches()){
            number.setError("Enter Valid Number");
            number.requestFocus();
            return;
        }

        if(mailS.isEmpty()) {
            mail.setError("Mail is required");
            mail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mailS).matches())
        {
            mail.setError("Enter valid Email");
            mail.requestFocus();
            return;
        }
        if(passS.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }
        if(passS.length()<5){
            pass.setError("Minimum length should be 5 ");
            pass.requestFocus();
        }

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(mailS, passS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(ANU.this, "User Successfully Created", Toast.LENGTH_SHORT).show();
                }
                else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                    progressDialog.dismiss();
                    Toast.makeText(ANU.this, "You are already registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mailS=mailS.replace('.',',');
         ref = FirebaseDatabase.getInstance().getReference().child("Users").child(mailS);
        //////////

        ref.child("data").setValue("Seller Name:"+nameS+"\n"+"Number:"+numberS+"\n"+"Address:"+addS);
        ref.child("Number").setValue(numberS);

        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);
    }


}
