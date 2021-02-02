package com.example.admin.BlockView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Retrieve extends AppCompatActivity {
    Button b1;
    TextView t1;
    FirebaseAuth auth;
    ProgrammingAdapter pAdapter;
    DatabaseReference ref, fref;
    FirebaseStorage sref = FirebaseStorage.getInstance();
    //StorageReference storageReference;
    RecyclerView list;
    Context context;

    // String mail=auth.getInstance().getCurrentUser().getEmail().replace('.',',');
    public String addToUData, tempUrl, userId;
    ProgressDialog progressDialog;
    ArrayList<String> usrData = new ArrayList<>();
    ArrayList<String> imgs = new ArrayList<>();
    ArrayList<String> nums = new ArrayList<>();
    String adr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        init();
        showDialog1();
        //list.setLayoutManager(new LinearLayoutManager(this));

        b1 = findViewById(R.id.b1);
        t1 = findViewById(R.id.txt1);
        list = findViewById(R.id.list);

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(manager);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRetrieve();

            }
        });
    }

    private void showDialog1() {
    }

    private void init() {
        this.progressDialog = new ProgressDialog(this);
    }

    private void onRetrieve() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        usrData.clear();
        imgs.clear();
        nums.clear();

        //CALLING PROGRAMMING ADAPTER.
        //usrData.clear();
        final String childS = t1.getText().toString();
        if (childS.contains("[") || childS.contains("]") || childS.contains("{") || childS.contains("}") || childS.contains(".") || childS.contains(",")) {

            t1.setError("Special symbols not allowed here.");
            progressDialog.dismiss();
            t1.requestFocus();
            return;
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, imgs);
        pAdapter = new ProgrammingAdapter(usrData, imgs, nums);
        list.setAdapter(pAdapter);

        // Read from the database

        ref = FirebaseDatabase.getInstance().getReference().child("AssetList");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Asnap : dataSnapshot.getChildren()) {
                    String Aval = Asnap.getKey();
                    fref = ref.child(Aval).child(childS);
                    fref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {


                                final ArrayList<String> temp = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    final String value = snapshot.getValue(String.class);
                                    if (snapshot.getKey().equals("Desc")) {
                                        temp.add(value);

                                    }
                                    if (snapshot.getKey().equals("Name")) {

                                        temp.add(value);


                                    }
                                    if (snapshot.getKey().equals("Price")) {
                                        temp.add(value);
                                    }
                                    if (snapshot.getKey().equals("email")) {
                                        temp.add("Email:" + value);
                                        addToUData = value.replace('.', ',');

                                    }
                                    if (snapshot.getKey().equals("sellerId")) {
                                        userId = value;
                                        //imgs.add(tempUrl);


                                    }
                                    if (snapshot.getKey().equals("url")) {
                                        tempUrl = value;
                                        imgs.add(tempUrl);
                                    }
//                                temp.add(value);


                                }
                                // pAdapter.notifyDataSetChanged();

                                DatabaseReference numRef = FirebaseDatabase.getInstance().getReference().child("Users").child(addToUData);
                                numRef.child("data").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        adr = (String) dataSnapshot.getValue();

                                        usrData.add((adr + temp).replace('[', ' ')
                                                .replace(']', ' ').replace(',', '\n')
                                                .replace('{', ' ').replace('}', '.'));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                numRef.child("Number").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        nums.add(String.valueOf(dataSnapshot.getValue()));
                                        Toast.makeText(Retrieve.this, nums.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Toast.makeText(Retrieve.this, "Product Not found please try again later.", Toast.LENGTH_SHORT).show();
                            }

                            pAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Retrieve.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }

                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

}