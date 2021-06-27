package com.example.patientscomm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    ArrayList<Question> list;

    private ItemClickListener mItemListener;

    public Adapter(ArrayList<Question> list, ItemClickListener itemClickListener) {
        this.list = list;
        this.mItemListener = itemClickListener;
    }

    public Adapter(ArrayList<Question> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new MyViewHolder(view, mItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(list.get(position).getQuestion());
        // holder.itemView.setOnClickListener(view -> {mItemListener.onItemClick(list.get(position));});//this will get the position item in RecyclerView

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   //INTERFACE FOR MAKE RECYCLER VIEW CLICKABLE
    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView question;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            question = itemView.findViewById(R.id.textViewQuestionResult);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,getAdapterPosition());

        }
    }
}
