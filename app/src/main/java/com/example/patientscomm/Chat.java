package com.example.patientscomm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.patientscomm.Adapter.MessageAdapter;
import com.example.patientscomm.Fragments.APIService;
import com.example.patientscomm.Model.ChatMessage;
import com.example.patientscomm.Model.User;
import com.example.patientscomm.Notifications.Client;
import com.example.patientscomm.Notifications.Data;
import com.example.patientscomm.Notifications.MyResponse;
import com.example.patientscomm.Notifications.Sender;
import com.example.patientscomm.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {
     CircleImageView profile_image;
     TextView name;
     TextView surname;

     FirebaseUser fuser;
     DatabaseReference messageReference;
     ImageButton btn_send;
     EditText text_send;

     MessageAdapter messageAdapter;
     List<ChatMessage> mChatMessage;
     RecyclerView  recyclerView;

     Intent intent;
      String userId;
     ValueEventListener seenListener;

     APIService apiService;

     boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);

        recyclerView  = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Toolbar chatToolbar =findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chatToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        profile_image = findViewById(R.id.chat_toolbar_profile_image2);
        name = findViewById(R.id.chat_toolbar_name);
        surname = findViewById(R.id.chat_toolbar_surname);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);


        intent = getIntent();
        //Final String userId
        userId = intent.getStringExtra("userId");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg =  text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),userId,msg);
                }else{
                    Toast.makeText(Chat.this,"You can't send empty message",Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });


        messageReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name.setText(user.getName());
                surname.setText(user.getSurname());
                 if(user.getImageURL().equals("default")){
                profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);}
                readMessages(fuser.getUid(),userId,user.getImageURL());
                        //user.getImageURL

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userId);
    }

    private void seenMessage(final  String userId){
        messageReference = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 :snapshot.getChildren()){
                    ChatMessage chatMessage = snapshot1.getValue(ChatMessage.class);
                    if(chatMessage.getReceiver().equals(fuser.getUid()) && chatMessage.getSender().equals(userId)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot1.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("chats").push().setValue(hashMap);

        //add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chatlist").child(fuser.getUid()).child(userId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(notify){
                   sendNotification(receiver,user.getName(),user.getSurname(),msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void sendNotification(String receiver,String name,String surname,String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 :snapshot.getChildren()){
                    Token token = snapshot1.getValue(Token.class);
                    Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,name+" "+surname+": "+message,"New Message",userId);
                    Sender sender = new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(Chat.this,"Failed!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readMessages(String myId,String userId,String imageurl){
        mChatMessage = new ArrayList<>();
        messageReference = FirebaseDatabase.getInstance().getReference("chats");
        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChatMessage.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ChatMessage chatMessage = snapshot1.getValue(ChatMessage.class);
                    if((chatMessage.getReceiver().equals(myId) && chatMessage.getSender().equals(userId)) || (chatMessage.getReceiver().equals(userId) && chatMessage.getSender().equals(myId))){
                        mChatMessage.add(chatMessage);
                    }
                    messageAdapter = new MessageAdapter(Chat.this,mChatMessage,imageurl);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private  void  status(String status){
        messageReference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        messageReference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageReference.removeEventListener(seenListener);
        status("offline");
    }
}