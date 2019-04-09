package com.example.inclass10;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Notes extends AppCompatActivity {

    TextView editTextNote;
    FloatingActionButton logOff;
    RecyclerView recyclerView;
    Button addNote;
   // RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //ArrayList<MessageObject> arrayList;

    String url = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editTextNote = findViewById(R.id.editTextNotes);
        logOff = findViewById(R.id.floatingActionButtonLogOff);
        recyclerView = findViewById(R.id.recycleView);
        addNote = findViewById(R.id.buttonAddNote);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new ChatRecycleAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextNote.getText().toString().equals("")){
                    editTextNote.setError("Please enter a note");
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
                String string = sharedPref.getString("token", "default_value");

               OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("text", editTextNote.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("x-access-token", string)
                        .post(formBody)
                        .build();
            }
        });



    }
}
