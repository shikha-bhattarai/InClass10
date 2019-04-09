package com.example.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


public class AddNote extends AppCompatActivity {
    TextView editTextNote;
    String url = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextNote = findViewById(R.id.editTextNote);

        findViewById(R.id.buttonPostIt).setOnClickListener(new View.OnClickListener() {
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
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(AddNote.this, "Unable to add note.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject noteRoot = new JSONObject(response.body().string());
                            JSONObject noteNote = noteRoot.getJSONObject("note");
                            String messageId = noteNote.getString("_id");
                            String message = noteNote.getString("text");
                            String userID = noteNote.getString("userId");
                            Intent intent = getIntent();
                            intent.putExtra(Notes.MESSAGE_KEY, message);
                            intent.putExtra(Notes.MESSAGEID_KEY, messageId);
                            intent.putExtra(Notes.USERID_KEY, userID);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }
    }

