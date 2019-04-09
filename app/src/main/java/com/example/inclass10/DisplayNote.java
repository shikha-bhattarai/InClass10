package com.example.inclass10;

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
import java.net.HttpURLConnection;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayNote extends AppCompatActivity {
    private static final String DISPLAY_NOTE_URL = "http:ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/get";

    String message;
    TextView displaySingleMessage;
    Button close;
    String jsonMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        setTitle("Your Note");

        displaySingleMessage = findViewById(R.id.textViewDisplaySingleMessage);
        close = findViewById(R.id.buttonDisplayClose);
        jsonMessage = "";

        if (getIntent() != null && getIntent().getExtras() != null) {
            message = getIntent().getExtras().getString(RecycleAdapter.DISPLAY_NOTE_KEY);
        }

        SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "default_value");
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(DISPLAY_NOTE_URL).newBuilder();
        urlBuilder.addQueryParameter("id", message);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("x-access-token", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DisplayNote.this, "Unable to display message.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject rootMessage = new JSONObject(response.body().string());
                        JSONObject noteObject = rootMessage.getJSONObject("note");
                        if (noteObject.has("text")) {
                            jsonMessage = noteObject.getString("text");
                            DisplayNote.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    displaySingleMessage.setText(jsonMessage);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DisplayNote.this, "Unable to display note", Toast.LENGTH_SHORT);
                    return;
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
