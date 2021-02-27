package com.diu.tourstravelsadmin.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.diu.tourstravelsadmin.Adapter.ShowPackageAdapter;
import com.diu.tourstravelsadmin.Model.ModelLike;
import com.diu.tourstravelsadmin.Model.ModelPackage;
import com.diu.tourstravelsadmin.Model.ModelUser;
import com.diu.tourstravelsadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    ShowPackageAdapter showPackageAdapter;
    DatabaseReference databaseReference;
    public ArrayList<ModelPackage> packages;
    public ArrayList<ModelUser> users;
    static MainActivity mainActivity;
    static String username;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tours & Travels Packages");
        SplashActivity.getInstance().finish();
        mainActivity=this;


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseUser==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        packages = new ArrayList<>();
        users = new ArrayList<>();


        recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5),true));
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        showPackageAdapter = new ShowPackageAdapter(getApplicationContext(),packages,users,MainActivity.this);
        recyclerView.setAdapter(showPackageAdapter);




        Query query = FirebaseDatabase.getInstance().getReference().child("packages");
        query.orderByKey().limitToFirst(100).addChildEventListener(new QueryForImages());

        Query query2 = FirebaseDatabase.getInstance().getReference().child("Users");
        query2.orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new QueryForUserName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.threedotmenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        switch (id){
            case R.id.itemPackage:
                Intent intent= new Intent(MainActivity.this,PackageActivity.class);
                startActivity(intent);
                break;
            case R.id.itemBookingList:
                Intent intent1= new Intent(MainActivity.this,BookingActivity.class);
                startActivity(intent1);
                break;
            case R.id.itemUserList:
                Intent intent2= new Intent(MainActivity.this,UserListActivity.class);
                startActivity(intent2);
                break;
            case R.id.itemLogOut:
                firebaseAuth.signOut();
                Intent intent3= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.itemReceiptList:
                Intent intent4= new Intent(MainActivity.this, ReceiptListActivity.class);
                startActivity(intent4);
                break;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseUser==null)
        {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    class QueryForImages implements ChildEventListener
    {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            final ModelPackage modelPackage = dataSnapshot.getValue(ModelPackage.class);
            /*showImagesAdapter.setValue(modelImage,);
            showImagesAdapter.notifyDataSetChanged();*/
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users/" + modelPackage.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ModelUser  modelUser= dataSnapshot.getValue(ModelUser.class);
                    showPackageAdapter.setValue(modelPackage,modelUser);
                    showPackageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            showPackageAdapter.notifyDataSetChanged();

            Query query2 = FirebaseDatabase.getInstance().getReference().child("likes");
            query2.orderByChild("imageKey").equalTo(modelPackage.getmKey()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ModelLike modelLike = dataSnapshot.getValue(ModelLike.class);
                    modelPackage.addLike();
                    if(modelLike.getUserID().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        modelPackage.hasUserLiked=true;
                        modelPackage.like_Key = dataSnapshot.getKey();
                    }
                    showPackageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    ModelLike modelLike = dataSnapshot.getValue(ModelLike.class);
                    modelPackage.removeLike();
                    if(modelLike.getUserID().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        modelPackage.hasUserLiked=false;
                        modelPackage.like_Key = null;
                    }
                    showPackageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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

    public void likeHelper(ModelPackage modelPackage)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if(!modelPackage.hasUserLiked)
        {
            modelPackage.hasUserLiked=true;
            ModelLike modelLike = new ModelLike(firebaseUser.getUid(),modelPackage.getmKey());
            String key = databaseReference.child("likes").push().getKey();
            databaseReference.child("likes").child(key).setValue(modelLike);
            modelPackage.like_Key=key;
        }
        else
        {
            modelPackage.hasUserLiked=false;
            if(modelPackage.like_Key!=null)
            {
                databaseReference.child("likes").child(modelPackage.like_Key).removeValue();
            }
        }

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class QueryForUserName implements ChildEventListener{

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);
            username= modelUser.getUsername();

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