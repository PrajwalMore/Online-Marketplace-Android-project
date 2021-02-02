package com.example.admin.BlockView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Delete extends AppCompatActivity {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AssetList");
    DatabaseReference delRef;
    FirebaseAuth auth;
    ListView listView;
    ArrayList<String> usrData = new ArrayList<>();
    ArrayList<String> reference = new ArrayList<>();
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        listView = findViewById(R.id.culist);
        progressDialog = new ProgressDialog(this);
        seeData();

    }


    private void seeData() {
progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging In");
        progressDialog.show();
        final String user = auth.getInstance().getCurrentUser().getEmail();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usrData);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usrData.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String val = dataSnapshot1.getKey();
                    //Toast.makeText(Delete.this, val, Toast.LENGTH_SHORT).show();
                    ref.child(val).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                String d = dataSnapshot2.getKey();
                                //Toast.makeText(Delete.this, d, Toast.LENGTH_LONG).show();
                                final DatabaseReference mailRef = ref.child(val).child(d).child("email");
                                final DatabaseReference dataRef = ref.child(val).child(d);
                                mailRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String temp = dataSnapshot.getValue(String.class);//.replace("Email address:","").replace("\n","");

                                        if (user.equals(temp)) {

                                            reference.add(mailRef.getParent().toString());

                                            dataRef.addValueEventListener(new ValueEventListener() {
                                                ArrayList<String> listItems = new ArrayList<>();

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                                                        if (st.getKey().equals("email")) {
                                                            String snap = (String) st.getValue();
                                                            snap = "Email Address:".concat(snap);
                                                            listItems.add(snap);


                                                        } else {
                                                            if (st.getKey().equals("sellerId") || st.getKey().equals("url")) {

                                                            } else {
                                                                String snap = (String) st.getValue();
                                                                listItems.add(snap);
                                                            }
                                                        }

                                                    }
                                                    usrData.add(String.valueOf(listItems).replace('[', ' ')
                                                            .replace(']', ' ').replace(',', '\n')
                                                            .replace('{', ' ').replace('}', '.'));


                                                    listItems.clear();
                                                    customAdapter customAdapter = new customAdapter();
                                                    listView.setAdapter(customAdapter);
                                                    customAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(Delete.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
    }

    class customAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return usrData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.datalayout, null);
            TextView textView = view.findViewById(R.id.textView6);
            textView.setText(usrData.get(i));
            Button button = view.findViewById(R.id.del);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        delRef = FirebaseDatabase.getInstance().getReferenceFromUrl(reference.get(i));
                        delRef.removeValue();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            return view;
        }
    }
}
