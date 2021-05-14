package com.bin.smart.za.Adpter.AdapterQuiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.smart.za.Model.CategoryQuiz;
import com.bin.smart.za.R;
import com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity;
import com.bin.smart.za.ui.DoctorFragment.AddExame_fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class AddAndShowCategoryDoctorAdapter extends RecyclerView.Adapter<AddAndShowCategoryDoctorAdapter.viewHolder> {

    private List<CategoryQuiz> cat_list;

    public AddAndShowCategoryDoctorAdapter(List<CategoryQuiz> cat_list) {
        this.cat_list = cat_list;
    }

    public void setNoCategory(List<CategoryQuiz> cat_list)
    {
        this.cat_list=cat_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_number_ofcategory,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        String title = cat_list.get(position).getName();
        holder.setData(title,position,this);

    }

    @Override
    public int getItemCount() {
        return cat_list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageButton ib_delete_category;
        private TextView category_name;
        private Dialog loadingDialog;
        private Dialog changSubjectDialog;
        private EditText ed_edit;
        private Button btn_edit;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.text_item_category_name);
            ib_delete_category = itemView.findViewById(R.id.ib_delete_category);
            loadingDialog= new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT
                    ,ViewGroup.LayoutParams.WRAP_CONTENT);

            changSubjectDialog= new Dialog(itemView.getContext());
            changSubjectDialog.setContentView(R.layout.dialog_chang_subject_name);
            changSubjectDialog.setCancelable(true);
            changSubjectDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT
                    ,ViewGroup.LayoutParams.WRAP_CONTENT);

            ed_edit = changSubjectDialog.findViewById(R.id.et_change_subject_name);

            btn_edit = changSubjectDialog.findViewById(R.id.btn_change_subject_name);




        }

        public void setData(String title, final int position,final AddAndShowCategoryDoctorAdapter adapter) {
            category_name.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddExame_fragment.selected_ctegory_index= position;
                    Intent intent = new Intent(itemView.getContext(), SetsQuizDoctorActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view)
                {
                    //to get the last category

                    ed_edit.setText(cat_list.get(position).getName());
                    changSubjectDialog.show();
                



                    return false;
                }
            });

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ed_edit.getText().toString().isEmpty())
                    {
                        ed_edit.setError("Enter your subject name");
                        ed_edit.requestFocus();
                        return;
                    }
                    UpdateSubjectName(ed_edit.getText().toString(),position,itemView.getContext(),adapter);
                }
            });

            ib_delete_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Subject")
                            .setMessage("Do you want delete this subject")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                deleteCategory(position,itemView.getContext(),adapter);

                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();
                    alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLUE);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(50,0,50,0);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setLayoutParams(params);


                }
            });

        }
        private void deleteCategory(final int id, final Context context, final AddAndShowCategoryDoctorAdapter adapter)
        {
            loadingDialog.show();

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            Map<String,Object> catDoc= new ArrayMap<>();
            int index =1;

            for ( int i =1 ; i <cat_list.size();i++)
            {
                if (i != id)
                {
                    catDoc.put("CAT"+String.valueOf(index)+"_ID",cat_list.get(i).getId());
                    catDoc.put("CAT"+String.valueOf(index)+"_NAME",cat_list.get(i).getName());
                    index++;
                }

            }
            catDoc.put("COUNT",index - 1);

            firestore.collection("QUIZ").document("categories")
                    .set(catDoc)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) 
                        {
                            Toast.makeText(context, "Subject Delete Successful", Toast.LENGTH_SHORT).show();
                            AddExame_fragment.categoryList.remove(id);

                            adapter.notifyDataSetChanged();

                            loadingDialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            System.out.println("Error *********** delete Subject"+e.getLocalizedMessage());
                            loadingDialog.dismiss();
                        }
                    });


        }
        private void UpdateSubjectName(final String catNewName, final int position, final Context context, final AddAndShowCategoryDoctorAdapter adapter)
        {
            changSubjectDialog.dismiss();
            loadingDialog.show();

            Map<String,Object> catData = new ArrayMap<>();
            catData.put("NAME",catNewName);

            final  FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("QUIZ").document(cat_list.get(position).getId())
                    .update(catData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Map<String,Object> catDoc = new ArrayMap<>();

                            catDoc.put("CAT"+String.valueOf(position+1)+"_NAME",catNewName);

                            firestore.collection("QUIZ").document("categories")
                                    .update(catDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(context, "Change Successful", Toast.LENGTH_SHORT).show();
                                            AddExame_fragment.categoryList.get(position).setName(catNewName);
                                            adapter.notifyDataSetChanged();

                                            loadingDialog.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {

                                            Toast.makeText(context, "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                            loadingDialog.dismiss();
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
    }


}
