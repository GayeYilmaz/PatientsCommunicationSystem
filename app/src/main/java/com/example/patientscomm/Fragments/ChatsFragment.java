package com.example.patientscomm.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patientscomm.Adapter.UserAdapter;
import com.example.patientscomm.Model.ChatMessage;
import com.example.patientscomm.Model.User;
import com.example.patientscomm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    ChatMessage chatMessage = snapshot1.getValue(ChatMessage.class);
                    if(chatMessage.getSender().equals( fuser.getUid())){
                        usersList.add(chatMessage.getReceiver());
                    }
                    if(chatMessage.getReceiver().equals(fuser.getUid())){
                        usersList.add(chatMessage.getSender());


                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
    private void readChats(){
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                //display 1 user from chats
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    for(String id: usersList){
                        if(user.getId().equals(id)){
                            if(mUsers.size() != 0){
                                for(User user1 : mUsers){
                                    if(!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }
                            }else{
                                mUsers.add(user);
                            }

                        }

                    }

                }

                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}