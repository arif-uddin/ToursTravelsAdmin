package com.diu.tourstravelsadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.diu.tourstravelsadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BookingDetailsActivity extends AppCompatActivity {

    TextView bookingId,dateOfBooking,bookingName,phoneNo,dateForBooking,packageName,placeName,days,adult,child,stayAmount,foodAmount,totalAmount,
            payment,bookingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Booking Details");


        bookingId=findViewById(R.id.bookingId);
        dateOfBooking=findViewById(R.id.dateOfBooking);
        phoneNo=findViewById(R.id.phoneNo);
        bookingName=findViewById(R.id.bookingName);
        dateForBooking=findViewById(R.id.dateForBooking);
        packageName=findViewById(R.id.packageName);
        placeName=findViewById(R.id.placeName);
        days=findViewById(R.id.days);
        adult=findViewById(R.id.adult);
        child=findViewById(R.id.child);
        stayAmount=findViewById(R.id.stayAmount);
        foodAmount=findViewById(R.id.foodAmount);
        totalAmount=findViewById(R.id.totalAmount);
        payment=findViewById(R.id.payment);
        bookingStatus=findViewById(R.id.bookingStatus);


        bookingId.setText("Booking No: "+getIntent().getExtras().getString("bookingId"));
        dateOfBooking.setText("Date of Booking: "+getIntent().getExtras().getString("dateOfBooking"));
        phoneNo.setText("Phone No: "+getIntent().getExtras().getString("phoneNo"));
        bookingName.setText("Booking Name: "+getIntent().getExtras().getString("bookingName"));
        dateForBooking.setText("Date for Booking: "+getIntent().getExtras().getString("dateForBooking"));
        packageName.setText("Package Name: "+getIntent().getExtras().getString("packageName"));
        placeName.setText("Location: "+getIntent().getExtras().getString("location"));
        days.setText("Days: "+getIntent().getExtras().getString("days"));
        adult.setText("Adult: "+getIntent().getExtras().getString("adult"));
        child.setText("Child: "+getIntent().getExtras().getString("child"));
        stayAmount.setText("Stay Cost: "+getIntent().getExtras().getString("stayAmount"));
        foodAmount.setText("Food Cost: "+getIntent().getExtras().getString("foodAmount"));
        totalAmount.setText("Total Cost: "+getIntent().getExtras().getString("totalAmount"));
        payment.setText("Payment: "+getIntent().getExtras().getString("payment"));
        bookingStatus.setText("Booking Status: "+getIntent().getExtras().getString("status"));

    }
}