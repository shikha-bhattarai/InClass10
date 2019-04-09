package com.example.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notes extends AppCompatActivity {


    TextView editTextNote;
    FloatingActionButton logOff;
    RecyclerView recyclerView;
    Button addNote;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<NoteObject> arrayList;
    static final int NOTE_KEY = 100;
    static final String MESSAGE_KEY = "message";
    static final String MESSAGEID_KEY = "messageID";
    static final String ARRAY_KEY = "arraykey";
    public static final String USERID_KEY = "userId" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editTextNote = findViewById(R.id.textViewNote);
        logOff = findViewById(R.id.floatingActionButtonLogOff);
        recyclerView = findViewById(R.id.recycleView);
        addNote = findViewById(R.id.buttonAddNote);

        arrayList = new ArrayList<>();
        getAllNotes();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycleAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notes.this, AddNote.class);
                startActivityForResult(intent, NOTE_KEY);
            }
        });

       logOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences =getSharedPreferences("token",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(Notes.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOTE_KEY && resultCode == Activity.RESULT_OK){
             String message = data.getExtras().getString(MESSAGE_KEY);
             String messageID = data.getExtras().getString(MESSAGEID_KEY);
            String userID = data.getExtras().getString(USERID_KEY);
             arrayList.add(new NoteObject(message, messageID, userID));
             adapter.notifyDataSetChanged();
        }
    }

    public void getAllNotes(){
        Log.d("hdddddddii", "hl333333333333333333333333333333333333333333lo");
        SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "default_value");
        String url = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("x-access-token", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Notes.this, "No Notes.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message, messageID, userID;
                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray jsonArray = root.getJSONArray("notes");
                    arrayList.clear();
                    for(int x = 0; x<jsonArray.length(); x++){
                        JSONObject resultObject = jsonArray.getJSONObject(x);
                        messageID = resultObject.getString("_id");
                        message = resultObject.getString("text");
                        userID = resultObject.getString("userId");
                        arrayList.add(new NoteObject(message, messageID, userID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
