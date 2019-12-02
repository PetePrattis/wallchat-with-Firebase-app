package com.unipi.p15120.mywallchat;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.javasampleapproach.firebaserealtimedb.models.Message;
//import com.javasampleapproach.firebaserealtimedb.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText etUser, etText;//the views
    Button bSend, bUser;
    ListView lvMessages;

    String user, message;

    private static final String TAG = "MainActivity";
    boolean unique;

    FirebaseDatabase firebaseDB;
    DatabaseReference db, dbU, dbM;//firebase instances

    public ArrayList<String> arrayList, al;
    public ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etText = findViewById(R.id.etText);
        bSend = findViewById(R.id.bSend);
        bUser = findViewById(R.id.bUser);
        lvMessages = findViewById(R.id.lvMessages);

        etUser.setText(user);
        if(user ==null){
            bUser.setText("Set");
            bSend.setEnabled(false);

        }
        else if (user != ""){
            etUser.setText(user);
            etUser.setEnabled(false);
            bUser.setText("Change");
            bSend.setEnabled(true);
        }
        firebaseDB = FirebaseDatabase.getInstance();

        dbM = firebaseDB.getReference("messages");
        db = firebaseDB.getReference();
        dbU = firebaseDB.getReference("users");

        unique = true;

        arrayList = new ArrayList<>();//the array list in which I will save the messages
        al = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);//an adapter which will adapt the arraylist into the listview using simple_list_item_1 method
        lvMessages.setAdapter(adapter);

        dbM.addChildEventListener(new ChildEventListener() {//create listener for the database
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {//when a new child is added

                Message chat = dataSnapshot.getValue(Message.class);//we receive the data snapshot
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date myDate = null;
                try {
                    myDate = format.parse(chat.getMsgTime());

                } catch (Exception e) {}
                arrayList.add(myDate + "\n" + chat.getMsgUser() +" :"+ chat.getMsgText());//and for every message we save it in the array list

                adapter.notifyDataSetChanged();//we notify the adapter to update the view
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void send(View view){//button send
        user = etUser.getText().toString();
        message = etText.getText().toString();
        String id = "";
        etText.setText("");

        if(!TextUtils.isEmpty(message)){//the message cant be empty
            id = dbM.push().getKey();
            Message chatmessage = new Message(id, message, user);//we call the Message class to return a Message item, which we can save in the database
            dbM.child(id).setValue(chatmessage);//we add the new message
            Toast.makeText(this,"Message sent!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"You have not written a message!", Toast.LENGTH_SHORT).show();
        }

    }

    public void change(View view){//button change and set name
        if(bUser.getText().toString().equals("Set")){
            user = etUser.getText().toString();
            String id = "";
            unique = true;//this would be used to check if username is unique
            /*dbU.addListenerForSingleValueEvent( //this is an effort to get all usernames in database and later check if new username is unique
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                            for(Map.Entry<String,String> entry: td.entrySet()){
                                //Toast.makeText(getApplicationContext(),"Key: "+entry.getKey()+" Value: "+entry.getValue(), Toast.LENGTH_SHORT).show();
                                try {
                                if(entry.equals(user)){
                                    Toast.makeText(getApplicationContext(),"Same", Toast.LENGTH_SHORT).show();
                                }

                                    JSONObject jsonObject = new JSONObject(entry.toString());
                                    jsonObject.getString("Uname");
                                    Toast.makeText(getApplicationContext(),"Value: "+jsonObject, Toast.LENGTH_SHORT).show();

                                    if (jsonObject.equals(user)) {
                                        unique = false;
                                    }
                                }catch (Exception e){}
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }
            );*/


            if(!TextUtils.isEmpty(user) && unique){
                id = dbU.push().getKey();
                User userrecord = new User(id, user);
                dbU.child(id).setValue(userrecord);
                Toast.makeText(this,"User recorded!", Toast.LENGTH_SHORT).show();
                bUser.setText("Change");
                bSend.setEnabled(true);
                etUser.setEnabled(false);
            }
            else{
                Toast.makeText(this,"Username empty or not unique!", Toast.LENGTH_SHORT).show();
            }

        }
        else if (bUser.getText().toString().equals("Change")){
            etUser.setText("");
            bUser.setText("Set");
            bSend.setEnabled(false);
            etUser.setEnabled(true);

        }



    }
}
