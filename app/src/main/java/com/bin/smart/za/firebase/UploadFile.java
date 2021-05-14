package com.bin.smart.za.firebase;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bin.smart.za.interfaces.OnListenerUploadFileFromMobileToFireStorage;
import com.bin.smart.za.ui.DoctorActivity.AddLectureDoctorActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadFile {

    private OnListenerUploadFileFromMobileToFireStorage onListenerUploadFileFromMobileToFireStorage;
    private StorageReference storageReference;
    private String fileName;
    private Uri data;
    private Uri uri;

    public UploadFile(OnListenerUploadFileFromMobileToFireStorage onListenerUploadFileFromMobileToFireStorage , StorageReference storageReference , Uri data) {
        this.onListenerUploadFileFromMobileToFireStorage = onListenerUploadFileFromMobileToFireStorage;
        this.storageReference = storageReference;
        this.data = data;
    }

    public void UploadFileFromMobile(){
        UploadTask uploadTask = storageReference.putFile(data);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    onListenerUploadFileFromMobileToFireStorage.uploadIsSuccessful();
                }else {
                    onListenerUploadFileFromMobileToFireStorage.uploadIsFailed();
                }
            }
        });
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        });
        uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    uri = task.getResult();
                }
            }
        });
    }
    public String getUri(){
        return uri.toString();
    }
}
