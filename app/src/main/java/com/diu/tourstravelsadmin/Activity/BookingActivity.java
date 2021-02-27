package com.diu.tourstravelsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diu.tourstravelsadmin.Adapter.BookingListAdapter;
import com.diu.tourstravelsadmin.Model.ModelBooking;
import com.diu.tourstravelsadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    RecyclerView recyclerVieworderList;
    BookingListAdapter bookingListAdapter;

    ArrayList<ModelBooking> bookings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Booking List");


        bookings = new ArrayList<>();
        recyclerVieworderList=findViewById(R.id.recyclerVieworderList);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("bookings");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        LinearLayoutManager linearVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerVieworderList.setLayoutManager(linearVertical);
        bookingListAdapter = new BookingListAdapter(getApplicationContext(),bookings);
        recyclerVieworderList.setAdapter(bookingListAdapter);


        Query query = FirebaseDatabase.getInstance().getReference().child("bookings");
        query.addChildEventListener(new QueryForBookings());

    }

    private class QueryForBookings implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            final ModelBooking modelBooking = dataSnapshot.getValue(ModelBooking.class);
            bookingListAdapter.setValues(modelBooking);
            bookingListAdapter.notifyDataSetChanged();

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