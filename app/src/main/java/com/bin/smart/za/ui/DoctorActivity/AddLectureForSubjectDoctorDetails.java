package com.bin.smart.za.ui.DoctorActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdabterDoctor.AdabteraddLectureDoctor;
import com.bin.smart.za.Adpter.AdabterStudent.AdapterLecture;
import com.bin.smart.za.Model.DownModel;
import com.bin.smart.za.Model.NumberLecture;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

public class AddLectureForSubjectDoctorDetails extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference ;
    private ArrayList<NumberLecture> arr = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private AdabteraddLectureDoctor adabteraddLectureDoctor;
    private Button btn_AddLecture;
    private StorageReference storageReference;
    private static final int VIDEO_REQUEST = 1;
    private Uri VideoUri;
    private ProgressBar progressBar;
    private String level;
    private String tirm ;
    private String subject ;
    //Pdf inflate
    EditText editPdfName;
    Button btn_uploadpdf;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture_for_subject_doctor_details);


    }

    private void UploadVideo()
    {
        progressBar.setVisibility(View.VISIBLE);
        if (VideoUri != null)
        {
            StorageReference reference = storageReference.child(System.currentTimeMillis()+
                    "."+getFileExt(VideoUri));
            reference.putFile(VideoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_SHORT).show();

                            collectionReference =firebaseFirestore.collection("years").document(level)
                                    .collection("tirm").document(tirm)
                                    .collection("Subject").document(subject)
                                    .collection("Lectures");


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddLectureForSubjectDoctorDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void ChooseVideo()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,VIDEO_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK &&
                data !=null && data.getData()!=null)

            VideoUri = data.getData();
    }

    private String getFileExt(Uri VideoUri)
    {
        ContentResolver contentResolver = getApplication().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(VideoUri));
    }
    //Pdf


    @Override
    protected void onStart() {
        super.onStart();
        arr.clear();

        final Intent intent = getIntent();
        subject = intent.getStringExtra("subjectName");
        level = intent.getStringExtra("level1");
        tirm = intent.getStringExtra("tirm1");
        saveData("subjectname" , subject);
        saveData("level1" , level);
        saveData("term" , tirm);



        adabteraddLectureDoctor= new AdabteraddLectureDoctor();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_showLectureAndAdd);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adabteraddLectureDoctor);
        btn_AddLecture = findViewById(R.id.btn_addLectue);

        btn_AddLecture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddLectureForSubjectDoctorDetails.this,AddLectureDoctorActivity.class);
                startActivity(intent1);
            }
        });

        collectionReference =firebaseFirestore.collection("years")
                .document(level)
                .collection("tirm")
                .document(tirm)
                .collection("Subject").document(subject)
                .collection("Lectures");

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for (int i =0 ; i < task.getResult().getDocuments().size() ; i++ )
                        {

                            String video =task.getResult().getDocuments().get(i).get("Video")+"";
                            String text =task.getResult().getDocuments().get(i).get("text")+"";
                            String pdf =task.getResult().getDocuments().get(i).get("pdf")+"";
                            String pdfName =task.getResult().getDocuments().get(i).get("pdfName")+"";
                            NumberLecture model = new NumberLecture(video,text,pdf,pdfName);

                            arr.add(model);

                        }
                        System.out.println("*********"+task.getResult().getDocuments().size());
                        adabteraddLectureDoctor.SetArryList(arr);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });


    }
    public void saveData(String key , String value)
    {
        sharedPreferences = AddLectureForSubjectDoctorDetails.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.commit();
    }
}