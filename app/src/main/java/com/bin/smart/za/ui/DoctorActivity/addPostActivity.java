package com.bin.smart.za.ui.DoctorActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bin.smart.za.Model.Admins;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class addPostActivity extends AppCompatActivity {


    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    EditText titleEt;
    ImageView imageIv;
    Button uploadBtn;
    DatabaseReference userDbRef;
    String name, email, uid, dp;
    ProgressDialog pd;
    String DoctorDetails;
    SharedPreferences sharedPreferences;


    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;
    private Uri Image_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        titleEt = findViewById(R.id.pTitleTv);
        uploadBtn = findViewById(R.id.PUdploadBtn);
        imageIv = findViewById(R.id.PImageIv);

        DoctorDetails = loadData("phoneDoctor");
        GetDetailsDoctor();


        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //     actionBar = getSupportActionBar();
//        actionBar.setTitle("Add New Post");
        //     actionBar.setDisplayShowHomeEnabled(true);
        //     actionBar.setDisplayHomeAsUpEnabled(true);
        //    actionBar.setSubtitle(email);

        //get info of cureent user to include in post

        //To Do
        /*
        userDbRef = FirebaseDatabase.getInstance().getReference("Admins");
        Query query = userDbRef.orderByChild("Email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */


//        checkUser();

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString().trim();


                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(addPostActivity.this, "Enter title...", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (Image_uri == null) {
                    //post without Image
                    uploadData(title, "noImage");
                } else {
                    //post with image
                    uploadData(title, String.valueOf(Image_uri));
                }

            }
        });

        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
    }

    private void uploadData(final String title, String uri) {
        pd.setMessage("Publishing Post...");
        pd.show();
        ;

        final String timestamp = String.valueOf(System.currentTimeMillis());

        String filePathAndName = "Posts/" + "post_" + timestamp;

        if (!uri.equals("noImage")) {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance()
                    .getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded in firebase storage
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                // uri is recieved uploaf
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid", uid);
                                hashMap.put("uName", name);
                                hashMap.put("uEmail", email);
                                hashMap.put("pId", timestamp);
                                hashMap.put("PTitle", title);
                                hashMap.put("PImage", downloadUri);
                                hashMap.put("pTime", timestamp);

                                //path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put in this ref
                                ref.child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added
                                                pd.dismiss();
                                                Toast.makeText(addPostActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
                                                titleEt.setText("");
                                                imageIv.setImageURI(null);
                                                Image_uri = null;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed  adding post in database
                                                pd.dismiss();
                                                Toast.makeText(addPostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    });

        }
        else {
            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uid", uid);
            hashMap.put("uName", name);
            hashMap.put("uEmail", email);
            hashMap.put("pId", timestamp);
            hashMap.put("PTitle", title);
            hashMap.put("PImage", "noImage");
            hashMap.put("pTime", timestamp);

            //path to store post data
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            //put in this ref
            ref.child(uid).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added
                            pd.dismiss();
                            Toast.makeText(addPostActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
                            titleEt.setText("");
                            imageIv.setImageURI(null);
                            Image_uri = null;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed  adding post in database
                            pd.dismiss();
                            Toast.makeText(addPostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void showImagePickDialog() {
        //option to pick img from
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle clicks
                        if (which == 0) {
                            //camera click
                            if (!CheckCameraPermissions()) {
                                RequestCameraPermission();
                            } else {
                                PickFromCamera();

                            }

                        } else {
                            //gallery click
                            if (!CheckStorePermission()) {
                                requestStoragePermission();
                            } else {
                                PickFromGallery();
                            }
                        }
                    }
                }).show();
    }

    private void PickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void PickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Group Image Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Group Image Description");
        Image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean CheckStorePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean CheckCameraPermissions() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void RequestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

//    private void checkUser() {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user != null) {
//            email = user.getEmail();
//            uid = user.getUid();
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //handle permission result
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean StorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (CameraAccepted && StorageAccepted) {
                        PickFromCamera();
                        ;
                    } else {
                        //both or one denied
                        Toast.makeText(this, "Camera & Storage Permission are required", Toast.LENGTH_SHORT).show();

                    }

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean StorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (StorageAccepted) {
                        //permission allowed
                        PickFromGallery();
                    } else {
                        //permission denied
                        Toast.makeText(this, "  Storage Permission required"
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //handle image pick result
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //was picked from gallery
                Image_uri = data.getData();
                //set to imageView
                imageIv.setImageURI(Image_uri);
            }
        } else if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //was picked camera
                //set to Imageview
                imageIv.setImageURI(Image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String loadData(String key) {
        sharedPreferences = addPostActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = sharedPreferences.getString(key, "000");
        return value;

    }

    public void GetDetailsDoctor()
    {

        final DatabaseReference Refreance;
        Refreance = FirebaseDatabase.getInstance().getReference("Admins")
                .child(DoctorDetails);
        Refreance.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
//                        for (DataSnapshot sp : snapshot.getChildren())
//                        {
//                            name =snapshot.child("doctorName").getValue()+"";
//                            email = snapshot.child("Email").getValue()+"";
//                            uid = snapshot.child("uidDoctor").getValue()+"";

                            HashMap<String ,String> fetchData =
                                    (HashMap<String, String>) snapshot.getValue();
                            name= fetchData.get("doctorName");
                            email= fetchData.get("Email");
                            uid = fetchData.get("uidDoctor");


//                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }

}