package com.example.firebaselearning;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    private Context context;
    private ArrayList<ModelClass> list;
    private RecyclerViewClickListener listener;

    public AdapterClass(Context context, ArrayList<ModelClass> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelClass modelClass = list.get(position);
        holder.subject.setText(modelClass.getSubject());
        holder.section.setText(modelClass.getSection());
        holder.ownerName.setText(modelClass.getOwnerName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView subject,section,ownerName;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            subject = itemView.findViewById(R.id.subjectcardID);
            section = itemView.findViewById(R.id.sectionCardID);
            ownerName = itemView.findViewById(R.id.ownerCardID);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
             listener.onClick(view, getAdapterPosition());
        }
    }
}