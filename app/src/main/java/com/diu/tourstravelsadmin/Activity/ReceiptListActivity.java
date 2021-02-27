package com.diu.tourstravelsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.diu.tourstravelsadmin.Adapter.ReceiptListAdapter;
import com.diu.tourstravelsadmin.Model.ModelPayment;
import com.diu.tourstravelsadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Objects;

public class ReceiptListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    static ReceiptListActivity receiptListActivity;

    RecyclerView recyclerView;
    ReceiptListAdapter receiptListAdapter;
    ArrayList<ModelPayment> payments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Receipt List");

        receiptListActivity=this;

        payments = new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("payments");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        LinearLayoutManager linearVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearVertical);
        receiptListAdapter = new ReceiptListAdapter(getApplicationContext(),payments);
        recyclerView.setAdapter(receiptListAdapter);


        Query query = FirebaseDatabase.getInstance().getReference().child("payments");
        query.addChildEventListener(new ReceiptListActivity.QueryForPayments());
    }

    private class QueryForPayments implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            final ModelPayment modelPayment = dataSnapshot.getValue(ModelPayment.class);
            receiptListAdapter.setValues(modelPayment);
            receiptListAdapter.notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}