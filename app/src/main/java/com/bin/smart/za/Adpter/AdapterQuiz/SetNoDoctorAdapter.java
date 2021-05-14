package com.bin.smart.za.Adpter.AdapterQuiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorFragment.AddExame_fragment;
import com.bin.smart.za.ui.McqExamDoctor.InsertQuestionDoctorActivity;
import com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Map;

import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.categoryList;
import static com.bin.smart.za.ui.DoctorFragment.AddExame_fragment.selected_ctegory_index;
import static com.bin.smart.za.ui.McqExamDoctor.SetsQuizDoctorActivity.selected_set_Index;

public class SetNoDoctorAdapter extends RecyclerView.Adapter<SetNoDoctorAdapter.ViewHolder>
{
    private List<String> listSet ;

    public SetNoDoctorAdapter(List<String> listSet) {
        this.listSet = listSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_number_ofcategory,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String setID = listSet.get(position);
        holder.SetData(position,setID,this);



    }

    @Override
    public int getItemCount() {
        return listSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageButton ib_delete_set;
        private TextView set_name;
        private Dialog loadingDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            set_name = itemView.findViewById(R.id.text_item_category_name);
            ib_delete_set = itemView.findViewById(R.id.ib_delete_category);

            loadingDialog= new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.progress_dialog_loding_sets_ofquiz);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT
                    ,ViewGroup.LayoutParams.WRAP_CONTENT);

        }

        public void SetData(final int position, final String setID , final SetNoDoctorAdapter adapter)
        {

            set_name.setText("SET"+String.valueOf(position+1));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selected_set_Index = position;

                    Intent intent = new Intent(itemView.getContext(), InsertQuestionDoctorActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            ib_delete_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Quiz")
                            .setMessage("Do you want delete this Quiz")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    deleteQuiz(position,setID,itemView.getContext(),adapter);

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

        private void deleteQuiz(final int position, String setID , final Context context, final SetNoDoctorAdapter adapter)
        {
            loadingDialog.show();

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("QUIZ").document(categoryList.get(selected_ctegory_index).getId())
                    .collection(setID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                        {
                            WriteBatch batch = firestore.batch();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots )
                            {
                                batch.delete(doc.getReference());
                            }
                            batch.commit()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Map<String , Object> catDoc = new ArrayMap<>();

                                            int index =1;
                                            for (int i = 0 ; i <listSet.size() ; i++)
                                            {
                                                if (i != position)
                                                {
                                                    catDoc.put("SET"+String.valueOf(index)+"_ID",listSet.get(i));
                                                    index++;
                                                }
                                            }

                                            catDoc.put("SETS",index - 1 );
                                             firestore.collection("QUIZ").document(categoryList.get(selected_ctegory_index).getId())
                                                     .update(catDoc)
                                                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                         @Override
                                                         public void onSuccess(Void aVoid)
                                                         {
                                                             Toast.makeText(context, "Delete Quiz Successful ", Toast.LENGTH_SHORT).show();

                                                             SetsQuizDoctorActivity.SetId.remove(position);
                                                             categoryList.get(selected_ctegory_index).setNoOfSets(String.valueOf(  SetsQuizDoctorActivity.SetId.size()));
                                                             adapter.notifyDataSetChanged();
                                                             loadingDialog.dismiss();

                                                         }
                                                     })
                                                     .addOnFailureListener(new OnFailureListener() {
                                                         @Override
                                                         public void onFailure(@NonNull Exception e) {
                                                             Toast.makeText(context, "Error when Delete Quiz  ", Toast.LENGTH_SHORT).show();
                                                             loadingDialog.dismiss();

                                                         }
                                                     });
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
    }
}
