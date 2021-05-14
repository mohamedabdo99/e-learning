package com.bin.smart.za.ui.DoctorActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdabterDoctor.AdabteraddLectureDoctor;
import com.bin.smart.za.Model.DownModel;
import com.bin.smart.za.Model.Lecture;
import com.bin.smart.za.Model.NumberLecture;
import com.bin.smart.za.R;
import com.bin.smart.za.firebase.UploadFile;
import com.bin.smart.za.interfaces.OnListenerUploadFileFromMobileToFireStorage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class AddLectureDoctorActivity extends AppCompatActivity
{

    public static final int PDF = 1;
    public static final int VIDEO = 2;
    public static final int VIDEO_REQUEST = 3;

    private Lecture lecture = new Lecture();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference ;
    private ArrayList<NumberLecture> arrayList = new ArrayList<>();
    private AdabteraddLectureDoctor adabteraddLectureDoctor;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference  = firebaseStorage.getReference();
    private EditText editPdfName,et_documentation;
    private Button btn_video , btn_uploadpdf ,btn_addLecture;
    private Uri VideoUri;
    private String uriFromPdf;
    private String level1;
    private String tirm1 ;
    private String subject1 ;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture_doctor);

        subject1 = loadData("subjectname");
        level1 = loadData("level1");
        tirm1 = loadData("term");
        editPdfName =findViewById(R.id.txt_pdfName);
        progressDialog=new ProgressDialog(this);
        et_documentation = findViewById(R.id.tv_documentation);
        btn_uploadpdf = findViewById(R.id.btn_upload_pdf);
        btn_addLecture = findViewById(R.id.Add_lecture);
        btn_uploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lecture.setState(PDF);
                selectPDFFile();
            }
        });
        btn_video = findViewById(R.id.btn_choseVideo);

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lecture.setState(VIDEO);
                ChooseVideo();
            }
        });

        btn_addLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                if (editPdfName == null)
//                {
//                    Toast.makeText(AddLectureDoctorActivity.this
//                            , "pdf name is required ", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (lecture.getVideo() == null)
//                {
//                    Toast.makeText(AddLectureDoctorActivity.this
//                            , "Check you chose the Video successful", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (lecture.getPdf() == null)
//                {
//                    Toast.makeText(AddLectureDoctorActivity.this
//                            , "Check you chose the pdf successful", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (et_documentation == null)
//                {
//                    Toast.makeText(AddLectureDoctorActivity.this
//                            , "documentation is required ", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                AddLecture();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            btn_addLecture.setEnabled(true);
            editPdfName.setText(data.getDataString().
                    substring(data.getDataString().lastIndexOf("/") + 1));

            if (lecture.getState() == PDF){
                uploadPDFFile(data.getData());
            }

            if (lecture.getState() == VIDEO)
            {
                UploadVideo(VideoUri=data.getData());
            }

        }
    }

    private void AddLecture()
    {
        final String LectureId = UUID.randomUUID().toString();
        final String docu = et_documentation.getText().toString().trim();
        final  String pdfName2 = editPdfName.getText().toString().trim();
        final Map<String,Object> addlecture = new ArrayMap<>();
        addlecture.put("Video",lecture.getVideo());
        addlecture.put("pdf", lecture.getPdf());
        addlecture.put("text",docu);
        addlecture.put("pdfName",pdfName2);
        addlecture.put("Id",LectureId );


      firebaseFirestore.collection("years").document(level1)
                .collection("tirm").document(tirm1)
                .collection("Subject").document(subject1)
                .collection("Lectures")
                .document(LectureId)
                .set(addlecture)
              .addOnSuccessListener(new OnSuccessListener<Void>()
              {
                  @Override
                  public void onSuccess(Void aVoid)
                  {

                                      Toast.makeText(AddLectureDoctorActivity.this
                                              , "add Success", Toast.LENGTH_SHORT).show();
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

          }
      });

    }

    public String loadData(String key)
    {
        sharedPreferences = AddLectureDoctorActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       String value = sharedPreferences.getString(key , "000");
       return value;

    }

    private void uploadPDFFile(Uri data)
    {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        final StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
         UploadTask uploadTask = reference.putFile(data);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddLectureDoctorActivity.this, "task is successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(AddLectureDoctorActivity.this, "task is failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
              return reference.getDownloadUrl();
            }
        });
        uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri = task.getResult();
                    lecture.setPdf(uri.toString());
                }
            }
        });

        /*
//        reference.putFile(data)
//                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//
//                        AddLecture(url,editPdfName.getText().toString());
//                        Toast.makeText(AddLectureDoctorActivity.this,"File Uploaded",Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//
//                progressDialog.setMessage("Uploaded:  "+(int)progress+"%");
//            }
//        });

         */
    }



    private void selectPDFFile()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }


    private void UploadVideo(Uri VideoUri)
    {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        if (VideoUri != null)
        {
            final StorageReference referenceVideo = storageReference.child("Lecture Videos"
                    +System.currentTimeMillis()+
                    "."+getFileExt(VideoUri));
            UploadTask uploadTask = referenceVideo.putFile(VideoUri);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddLectureDoctorActivity.this
                                , "task is successful", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(AddLectureDoctorActivity.this
                                , "task is failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return referenceVideo.getDownloadUrl();
                }
            });
            uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        lecture.setVideo(uri.toString());
                    }
                }
            });
        }
    }
    private void ChooseVideo()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }
    private String getFileExt(Uri VideoUri)
    {
        ContentResolver contentResolver = getApplication().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(VideoUri));
    }

}