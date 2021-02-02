package com.example.admin.BlockView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Main3Transact extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST=1;
    EditText aname,aprice,acate,adesc;
    Button submit;
ProgressDialog progressDialog;
    ImageView img;
    CardView cardView;
    DatabaseReference ref;
    StorageReference storageRef;
    FirebaseAuth auth;
    String user= auth.getInstance().getCurrentUser().getEmail();
    private Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_transact);
        cardView=findViewById(R.id.cardView);
        img=findViewById(R.id.imageView4);
        aname=findViewById(R.id.AssetName);
        aprice=findViewById(R.id.amount);
        acate=findViewById(R.id.cateText);
        adesc=findViewById(R.id.descText);


        progressDialog = new ProgressDialog(this);
        submit=findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });

    }



    private void fileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK &&data!=null && data.getData()!=null){
            imgUri=data.getData();
            File file=new File(String.valueOf(imgUri));
            //aname.setText(s);
            //Picasso.get().load(imgUri).into(img);
            img.setImageURI(imgUri);
            // FirebaseStorage sref = FirebaseStorage.getInstance();
            //StorageReference storageReference = sref.getReferenceFromUrl("gs://blockview-aed78.appspot.com/Images/Harley/5.1.png");

        }
    }
    private  String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((cr.getType(uri)));
    }


    private void onSubmit() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        if(imgUri!=null)
        {
            final String nameS,priceS,descS,cateS;
            nameS=aname.getText().toString().trim();
            priceS=aprice.getText().toString().trim();
            descS=adesc.getText().toString().trim();
            cateS=acate.getText().toString().trim();

            if(nameS.isEmpty()) {
                aname.setError("Name is required");
                progressDialog.dismiss();
                aname.requestFocus();
                return;
            }
            if(priceS.isEmpty()) {
                aprice.setError("Mail is required");
                progressDialog.dismiss();
                aprice.requestFocus();
                return;
            }
            if(descS.isEmpty()) {
                adesc.setError("Mail is required");
                progressDialog.dismiss();
                adesc.requestFocus();
                return;
            }
            if(cateS.isEmpty()) {
                acate.setError("Mail is required");
                progressDialog.dismiss();
                acate.requestFocus();
                return;
            }
            FirebaseUser ID = FirebaseAuth.getInstance().getCurrentUser();
            String uid = ID.getUid();
            long l=System.currentTimeMillis();
            ref = FirebaseDatabase.getInstance().getReference().child("AssetList"). push();
            ref.child(nameS).child("Desc").setValue("Description:"+descS);
            ref.child(nameS).child("email").setValue(user);
            ref.child(nameS).child("Price").setValue("Price:"+priceS);
            ref.child(nameS).child("sellerId").setValue(uid);

            ref.child(nameS).child("Name").setValue("Product Name:"+nameS);

            String usr=uid+"/"+nameS;
            //ref.child(nameS).child("url").setValue(l+","+getExtension(imgUri));
            storageRef = FirebaseStorage.getInstance().getReference("Images/"+usr);

            final StorageReference sref=storageRef.child(l+"."+getExtension(imgUri));

            sref.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ref.child(nameS).child("url").setValue(uri.toString());

                                }
                            });

                            progressDialog.dismiss();
                            Toast.makeText(Main3Transact.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(Main3Transact.this, "Exception", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        else{
            progressDialog.dismiss();
            Toast.makeText(this, "Please select image.", Toast.LENGTH_SHORT).show();
        }

    }

}

