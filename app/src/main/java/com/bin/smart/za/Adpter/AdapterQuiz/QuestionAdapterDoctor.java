package com.bin.smart.za.Adpter.AdapterQuiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.smart.za.Model.QuestionDoctorModel;
import com.bin.smart.za.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.SetId;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.selected_set_Index;

public class QuestionAdapterDoctor extends RecyclerView.Adapter<QuestionAdapterDoctor.ViewHolder> {

    public static List<QuestionDoctorModel> questionDoctorModels;

    public QuestionAdapterDoctor(List<QuestionDoctorModel> questionDoctorModels) {
        this.questionDoctorModels = questionDoctorModels;
    }

    @NonNull
    @Override
    public QuestionAdapterDoctor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_number_ofcategory,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapterDoctor.ViewHolder holder, int position)
    {
        holder.SetData(position,this);

    }

    @Override
    public int getItemCount() {
        return questionDoctorModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView_title;
        private ImageButton imageButton_delete;
        private Dialog loadingDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.text_item_category_name);
            imageButton_delete = itemView.findViewById(R.id.ib_delete_category);

            loadingDialog= new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT
                    ,ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void SetData(final int position, final QuestionAdapterDoctor adapter)
        {

            textView_title.setText("QUESTION"+ (position+1));



            imageButton_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Subject")
                            .setMessage("Do you want delete this subject")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    deleteQuestion(position,itemView.getContext(),adapter);

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

        private void deleteQuestion(final int position, final Context context , final QuestionAdapterDoctor adapter)
        {
            loadingDialog.show();

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("QUIZ").document(categoryList.get(selected_ctegory_index).getId())
                    .collection(SetId.get(selected_set_Index)).document(questionDoctorModels.get(position).getQuestionId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Map<String,Object> quesDoc = new ArrayMap<>();
//                            int index =0;
                            for (int i =0 ; i <questionDoctorModels.size() ; i++)
                            {
                                if (i != position)
                                {
                                    quesDoc.put("Q"+String.valueOf(i)+"_ID",questionDoctorModels.get(position).getQuestionId());
//                                    index++;
                                }
                            }
                            questionDoctorModels.remove(position);
                            quesDoc.put("COUNT",String.valueOf(questionDoctorModels.size()));

                            firestore.collection("QUIZ").document(categoryList.get(selected_ctegory_index).getId())
                                    .collection(SetId.get(selected_set_Index)).document("QUESTION_LIST")
                                    .set(quesDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(context, "Delete Question Successful", Toast.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
                                            loadingDialog.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Toast.makeText(context, "Delete Question Error"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
