package com.example.patientscomm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    ArrayList<Comment> list;
    DatabaseReference databaseUserRetrieve;
    private CommentClickListener commentClickListener;
    public CommentAdapter(ArrayList<Comment> list, CommentAdapter.CommentClickListener commentClickListener) {
        this.list = list;
        this.commentClickListener = commentClickListener;
    }

    public CommentAdapter(ArrayList<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new MyViewHolder(view, commentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.comment.setText(list.get(position).getComment());
        databaseUserRetrieve = FirebaseDatabase.getInstance().getReference().child("users").child(list.get(position).getUserId());
        databaseUserRetrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namex = snapshot.child("name").getValue().toString();
                String surnamex = snapshot.child("surname").getValue().toString();
                holder.name.setText(namex+""+surnamex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       // holder.name.setText(list.get(position).getUserId();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface CommentClickListener {
        void onCommentClick(View v, int position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView comment;
        TextView name;

        CommentClickListener commentClickListener;

        public MyViewHolder(@NonNull View itemView,CommentClickListener commentClickListener) {
            super(itemView);
            comment = itemView.findViewById(R.id.commentLayoutComment);
            name = itemView.findViewById(R.id.commentLayoutName);

            this.commentClickListener = commentClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            commentClickListener.onCommentClick(v,getAdapterPosition());

        }
    }
}
