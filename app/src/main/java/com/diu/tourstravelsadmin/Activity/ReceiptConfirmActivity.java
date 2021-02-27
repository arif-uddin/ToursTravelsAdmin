package com.diu.tourstravelsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diu.tourstravelsadmin.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ReceiptConfirmActivity extends AppCompatActivity {

    TextView bookingId,packageName,phoneNo,bookingName,dateOfBooking,totalAmount,paymentDate,trxId,paymentStatus;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_confirm);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Confirm Payment");

        bookingId=findViewById(R.id.bookingId);
        packageName=findViewById(R.id.packageName);
        phoneNo=findViewById(R.id.phoneNo);
        bookingName=findViewById(R.id.bookingName);
        dateOfBooking=findViewById(R.id.dateOfBooking);
        totalAmount=findViewById(R.id.totalAmount);
        paymentDate=findViewById(R.id.paymentDate);
        trxId=findViewById(R.id.trxId);
        paymentStatus=findViewById(R.id.paymentStatus);
        btnConfirm=findViewById(R.id.btnConfirm);

        bookingId.setText("Booking No: "+getIntent().getExtras().getString("bookingId"));
        packageName.setText("Package Name: "+getIntent().getExtras().getString("packageName"));
        phoneNo.setText("Phone No: "+getIntent().getExtras().getString("phoneNo"));
        bookingName.setText("Booking Name: "+getIntent().getExtras().getString("bookingName"));
        dateOfBooking.setText("Date of Booking: "+getIntent().getExtras().getString("dateOfBooking"));
        totalAmount.setText("Total Amount: "+getIntent().getExtras().getString("totalAmount"));
        paymentDate.setText("Date of Payment: "+getIntent().getExtras().getString("dateOfPayment"));
        trxId.setText("TrxId: "+getIntent().getExtras().getString("trxId"));
        paymentStatus.setText("Payment Status: "+getIntent().getExtras().getString("paymentStatus"));

        if (getIntent().getExtras().getString("paymentStatus").equals("Confirmed")){
            btnConfirm.setVisibility(View.GONE);
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = FirebaseDatabase.getInstance().getReference().child("payments");
                query.orderByKey().equalTo(getIntent().getExtras().getString("paymentId")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("payments");
                        databaseReference.child(getIntent().getExtras().getString("paymentId")).
                                child("paymentStatus").setValue("Confirmed");

                        Query query = FirebaseDatabase.getInstance().getReference().child("bookings");
                        query.orderByKey().equalTo(getIntent().getExtras().getString("bookingId")).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("bookings");
                                databaseReference.child(getIntent().getExtras().getString("bookingId")).
                                        child("bookingStatus").setValue("Confirmed");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(ReceiptConfirmActivity.this, "Done", Toast.LENGTH_SHORT).show();
                ReceiptListActivity.receiptListActivity.finish();
                finish();
            }
        });
    }

}