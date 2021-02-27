package com.diu.tourstravelsadmin.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diu.tourstravelsadmin.Model.ModelPackage;
import com.diu.tourstravelsadmin.Model.ModelUser;
import com.diu.tourstravelsadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class PackageDetailsActivity extends AppCompatActivity {

    TextView likes,packageName,placeName,description,days,adult,child,stayAmount,foodAmount,busAmount,trainAmount,airlinesAmount;
    ImageView im_images;

    ModelPackage modelPackage;
    private String imageUrl;
    private String UserId;

    ArrayList<ModelUser> users;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //@SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Package Details");

        likes=findViewById(R.id.likes);
        packageName=findViewById(R.id.packageName);
        placeName=findViewById(R.id.placeName);
        description=findViewById(R.id.description);
        days=findViewById(R.id.days);
        adult=findViewById(R.id.adult);
        child=findViewById(R.id.child);
        stayAmount=findViewById(R.id.stayAmount);
        foodAmount=findViewById(R.id.foodAmount);
        busAmount=findViewById(R.id.busAmount);
        trainAmount=findViewById(R.id.trainAmount);
        airlinesAmount=findViewById(R.id.airlinesAmount);
        im_images=findViewById(R.id.im_images);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("comments");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        modelPackage= new ModelPackage();


        imageUrl=getIntent().getExtras().getString("imageThumb");
        UserId=getIntent().getExtras().getString("userId");
        //details view of post
        Glide
                .with(getApplicationContext())
                .load(imageUrl)
                .into(im_images);

        description.setText("Description: "+getIntent().getExtras().getString("description"));
        placeName.setText("Location: "+getIntent().getExtras().getString("place"));
        packageName.setText("Package Name: "+getIntent().getExtras().getString("packageName"));
        days.setText("Days: "+getIntent().getExtras().getString("days"));
        adult.setText("Adult: "+getIntent().getExtras().getString("adult"));
        child.setText("Child: "+getIntent().getExtras().getString("child"));
        stayAmount.setText("Stay Cost [for given days]: "+getIntent().getExtras().getString("stayAmount")+" BDT");
        busAmount.setText("Bus Cost: "+getIntent().getExtras().getString("busAmount")+" BDT");
        trainAmount.setText("Train Cost: "+getIntent().getExtras().getString("trainAmount")+" BDT");
        airlinesAmount.setText("Air Fare: "+getIntent().getExtras().getString("airlinesAmount")+" BDT");
        foodAmount.setText("Food Cost: "+getIntent().getExtras().getString("foodAmount")+" BDT");
        likes.setText("Total Like(s): "+getIntent().getExtras().getString("like_counter"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.deletemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        switch (id){
            case R.id.itemDelete:
                DeletePackage();
                break;
        }

        return true;
    }

    private void DeletePackage() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("packages");
        databaseReference.child(getIntent().getExtras().getString("mKey")).removeValue();
        Toast.makeText(PackageDetailsActivity.this, "Done", Toast.LENGTH_SHORT).show();
        finish();
        MainActivity.mainActivity.finish();
        Intent intent= new Intent(PackageDetailsActivity.this,MainActivity.class);
        startActivity(intent);
    }

}