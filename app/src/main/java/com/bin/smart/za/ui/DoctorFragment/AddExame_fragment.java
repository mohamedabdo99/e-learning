package com.bin.smart.za.ui.DoctorFragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.bin.smart.za.Adpter.AdapterQuiz.AddAndShowCategoryDoctorAdapter;
import com.bin.smart.za.Adpter.AdapterQuiz.CategorySupjectaddExamAdapter;
import com.bin.smart.za.Model.CategoryQuiz;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentShowExam.EnterExamStudentActivity;
import com.bin.smart.za.ui.StudentShowExam.SetsNumberOfQuiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AddExame_fragment extends Fragment
{
    private RecyclerView recyclerView;
    private Toolbar toolbar_addSubject;
    private Button btn_AddNewSubject;
    private FirebaseFirestore firebaseFirestore;
    public static List<CategoryQuiz> categoryList = new ArrayList<>();
    public static int selected_ctegory_index = 0;
    private  AddAndShowCategoryDoctorAdapter adapter;
    private Dialog dialog;
    private EditText dialogEt_addCategory;
    private Button dialog_BtnAddCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_exame_fragment, container, false);

        toolbar_addSubject = view.findViewById(R.id.toolPr_doctor_addExam);
        toolbar_addSubject.setTitle("Subjects");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_addSubject);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.add_subject_category_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        recyclerView = view.findViewById(R.id.recycle_exam_NCategory);
        btn_AddNewSubject = view.findViewById(R.id.add_newSubject_exam);

        dialogEt_addCategory = dialog.findViewById(R.id.et_addSubject_dialog);
        dialog_BtnAddCategory = dialog.findViewById(R.id.btn_addSubject_dialog);

        firebaseFirestore = FirebaseFirestore.getInstance();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

         adapter = new AddAndShowCategoryDoctorAdapter(categoryList);
        recyclerView.setAdapter(adapter);
        LoadDataCategory();

        btn_AddNewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEt_addCategory.getText().clear();
                dialog.show();
            }
        });

        dialog_BtnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogEt_addCategory.getText().toString().isEmpty())
                {
                    dialogEt_addCategory.setError("Enter Subject Name");
                    dialogEt_addCategory.requestFocus();
                    return;
                }

                addNewCategorySubject(dialogEt_addCategory.getText().toString());
            }
        });



        return view;
    }

    private void addNewCategorySubject(final String title)
    {
        dialog.dismiss();

        Map<String,Object> catMap = new ArrayMap<>();

        catMap.put("NAME",title);
        catMap.put("SETS",0);
        catMap.put("COUNTER","1");

        final String doc_id = firebaseFirestore.collection("QUIZ").document().getId();

        firebaseFirestore.collection("QUIZ").document(doc_id)
                .set(catMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Map<String,Object> catDoc= new ArrayMap<>();
                        catDoc.put("CAT"+String.valueOf(categoryList.size()+1)+ "_NAME",title);
                        catDoc.put("CAT"+String.valueOf(categoryList.size()+1)+ "_ID",doc_id);
                        catDoc.put("COUNT",categoryList.size()+1);

                        firebaseFirestore.collection("QUIZ").document("categories")
                                .update(catDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(getContext(), "Subject Add Successful", Toast.LENGTH_SHORT).show();

                                        categoryList.add(new CategoryQuiz(doc_id,title,"0","1"));
                                        adapter.notifyItemInserted(categoryList.size());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void LoadDataCategory()
    {
        categoryList.clear();
        firebaseFirestore.collection("QUIZ").document("categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists())
                    {
                        long counter = (long)doc.get("COUNT");

                        for (int i =1 ; i<=counter;i++)
                        {
                            String catName = doc.getString("CAT" + i+ "_NAME");
                            String catId = doc.getString("CAT"+ i+ "_ID");

                            categoryList.add(new CategoryQuiz(catId,catName,"0","1"));
                        }
                        adapter.setNoCategory(categoryList);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "No Quiz Yet", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("***********************************");
            }
        });

    }

}