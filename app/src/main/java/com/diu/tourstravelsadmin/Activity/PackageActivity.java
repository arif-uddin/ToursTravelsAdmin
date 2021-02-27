package com.diu.tourstravelsadmin.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.diu.tourstravelsadmin.Model.ModelPackage;
import com.diu.tourstravelsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class PackageActivity extends AppCompatActivity {

    EditText packageName,placeName,adult,child,description,days,stayAmount,foodAmount,busAmount,trainAmount,airlinesAmount;
    Button mButtonUpload,mButton_file_browse;
    ProgressBar progressBar;
    ImageView mImageView;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 2;

    private StorageTask mUploadTask;
    String deviceModel=null;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Package");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("packages");
        firebaseAuth = FirebaseAuth.getInstance();

        packageName=findViewById(R.id.packageName);
        placeName=findViewById(R.id.placeName);
        adult=findViewById(R.id.adult);
        child=findViewById(R.id.child);
        description=findViewById(R.id.description);
        days=findViewById(R.id.days);
        stayAmount=findViewById(R.id.stayAmount);
        foodAmount=findViewById(R.id.foodAmount);
        busAmount=findViewById(R.id.busAmount);
        trainAmount=findViewById(R.id.trainAmount);
        airlinesAmount=findViewById(R.id.airlinesAmount);
        mButtonUpload=findViewById(R.id.btnSubmit);
        mButton_file_browse=findViewById(R.id.file_browse);
        progressBar=findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.image_view);

        mButton_file_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(PackageActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            //mImageView.setImageURI(mImageUri);
            if (data.getDataString() != null)
            {
                mButton_file_browse.setVisibility(ImageButton.GONE);
                mImageView.setVisibility(ImageView.VISIBLE);
            }

        }

        Glide.
                with(getApplicationContext())
                .load(mImageUri)
                .into(mImageView);


    }


    private void openFileChooser() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

//    private void addPackage() {
//
//        ModelPackage modelPackage= new ModelPackage(PackageName,PlaceName,Adult,Child,Description,Days,StayAmount,FoodAmount,BusAmount,TrainAmount,AirlinesAmount);
//        mDatabaseRef.child(String.valueOf(UUID.randomUUID())).setValue(modelPackage).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                if(task.isSuccessful()){
//                    Toast.makeText(PackageActivity.this,"Package has been added!",Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
//                    Intent intent= new Intent(PackageActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else {
//                    Toast.makeText(PackageActivity.this,"Failed to add package!",Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//
//            }
//        });
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    private String getExifInfo(Uri uri)
    {
        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(uri);
            ExifInterface exifInterface = new ExifInterface(in);
            deviceModel = exifInterface.getAttribute(ExifInterface.TAG_MAKE)+" "+exifInterface.getAttribute(ExifInterface.TAG_MODEL) ;
        } catch (IOException e) {
            e.getStackTrace();
            Log.e("ExifActivity", e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        Log.d("ExifActivity", "Device: " + deviceModel);
        return deviceModel;
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    private void uploadFile() {

        final String PackageName=packageName.getText().toString().trim();
        final String PlaceName=placeName.getText().toString().trim();
        final String Adult=adult.getText().toString().trim();
        final String Child=child.getText().toString().trim();
        final String Description=description.getText().toString().trim();
        final String Days=days.getText().toString().trim();
        final String StayAmount=stayAmount.getText().toString().trim();
        final String FoodAmount=foodAmount.getText().toString().trim();
        final String BusAmount=busAmount.getText().toString().trim();
        final String TrainAmount=trainAmount.getText().toString().trim();
        final String AirlinesAmount=airlinesAmount.getText().toString().trim();

        if(PackageName.isEmpty()) {
            packageName.setError("Package name is required");
            packageName.requestFocus();
            return;
        }

        if(PlaceName.isEmpty()) {
            placeName.setError("Place name is required");
            placeName.requestFocus();
            return;
        }

        if(Adult.isEmpty()) {
            adult.setError("Adult no. is required");
            adult.requestFocus();
            return;
        }

        if(Child.isEmpty()) {
            child.setError("Child no. is required");
            child.requestFocus();
            return;
        }

        if(Description.isEmpty()) {
            description.setError("Description is required");
            description.requestFocus();
            return;
        }

        if(Days.isEmpty()) {
            days.setError("Days is required");
            days.requestFocus();
            return;
        }

        if(StayAmount.isEmpty()) {
            stayAmount.setError("Stay amount is required");
            stayAmount.requestFocus();
            return;
        }

        if(FoodAmount.isEmpty()) {
            foodAmount.setError("Food amount is required");
            foodAmount.requestFocus();
            return;
        }

        if(BusAmount.isEmpty()) {
            busAmount.setError("Bus amount is required");
            busAmount.requestFocus();
            return;
        }

        if(TrainAmount.isEmpty()) {
            trainAmount.setError("Train amount is required");
            trainAmount.requestFocus();
            return;
        }

        if(AirlinesAmount.isEmpty()) {
            airlinesAmount.setError("Airlines amount is required");
            airlinesAmount.requestFocus();
            return;
        }

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child("images").child(System.currentTimeMillis() + "_" + FirebaseAuth.getInstance().getUid()
                    + "." + getFileExtension(mImageUri));

            final StorageReference fileReference2 = mStorageRef.child("thumbs").child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            getExifInfo(mImageUri);
            final byte[] data = compressImage();

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final Uri mainUri = uri;

                                    fileReference2.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            fileReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String uploadId = mDatabaseRef.push().getKey();
                                                    ModelPackage modelPackage= new ModelPackage(mainUri.toString(),uri.toString(),uploadId,firebaseAuth.getUid(),PackageName,PlaceName,Adult,Child,Description,Days,StayAmount,FoodAmount,BusAmount,TrainAmount,AirlinesAmount);
                                                    mDatabaseRef.child(uploadId).setValue(modelPackage);
                                                    Toast.makeText(PackageActivity.this, "Package added successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                            progressBar.setProgress((int) progress);
                                            if (progress == 100) {
                                                Toast.makeText(PackageActivity.this, "Package added successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(PackageActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            Log.e("Prr", "" + progress);
                                        }
                                    });


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PackageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    byte[] compressImage()
    {
        mButtonUpload.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        return  data;
    }
}