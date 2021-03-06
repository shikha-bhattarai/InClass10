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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Notes extends AppCompatActivity implements RecycleAdapter.UpdateAdapter {


    private TextView userName;
    private FloatingActionButton logOff;
    private RecyclerView recyclerView;
    private Button addNote;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<NoteObject> arrayList;
    private static final int NOTE_KEY = 100;
    static final String MESSAGE_KEY = "message";
    static final String MESSAGEID_KEY = "messageID";
    static final String ARRAY_KEY = "arraykey";
    public static final String USERID_KEY = "userId" ;
    private SharedPreferences sharedPref;
    private String userNameString;
    private String token;
    private static final String GETALLNOTES_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall";
    private static final String GET_USER_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Notes");

        userName = findViewById(R.id.textViewUserName);
        logOff = findViewById(R.id.floatingActionButtonLogOff);
        recyclerView = findViewById(R.id.recycleView);
        addNote = findViewById(R.id.buttonAddNote);
        sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "default_value");

        arrayList = new ArrayList<>();
        getAllNotes();


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycleAdapter(arrayList, Notes.this);
        recyclerView.setAdapter(adapter);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{

        }
        getUserName();


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
                SharedPreferences.Editor editor = sharedPref.edit();
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

    private void getAllNotes(){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GETALLNOTES_URL)
                .header("x-access-token", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Notes.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Notes.this, "No Notes", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String message, messageID, userID;
                    try {
                        JSONObject root = new JSONObject(response.body().string());
                        JSONArray jsonArray = root.getJSONArray("notes");
                        arrayList.clear();
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject resultObject = jsonArray.getJSONObject(x);
                            messageID = resultObject.getString("_id");
                            message = resultObject.getString("text");
                            userID = resultObject.getString("userId");
                            arrayList.add(new NoteObject(message, messageID, userID));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Notes.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Notes.this, "No Notes to Load. Add some notes", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getUserName(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GET_USER_URL)
                .header("x-access-token", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Notes.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Notes.this, "No User", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONObject root = new JSONObject(response.body().string());
                        userNameString = root.getString("name");
                        Notes.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userName.setText(userNameString);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Notes.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Notes.this, "No User", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void updateAdapter(int index) {
        Notes.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayList.remove(index);
                adapter.notifyItemRemoved(index);
            }
        });

    }
}
