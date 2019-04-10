/*
Teena Xiong & Shikha Bhattarai
In Class 10
 */

package com.example.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button signUp;
    private Button login;
    private EditText email;
    private EditText password;
    private boolean auth44;
    private final String LOGIN_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        signUp = findViewById(R.id.buttonSignupMain);
        login = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        auth44 = true;
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Please enter an email");
                    return;
                }

                if( password.getText().toString().trim().equals("")){
                    password.setError("Please enter a password");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    email.setError("Please enter a valid email");
                    return;
                }

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email.getText().toString().trim())
                        .add("password", password.getText().toString().trim())
                        .build();

                Request request = new Request.Builder()
                        .url(LOGIN_URL)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Unable to log in", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String token;
                            try {
                                JSONObject root = new JSONObject(response.body().string());
                                    token = root.getString("token");
                                        SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("token", token);
                                        editor.apply();
                                        Intent intent = new Intent(MainActivity.this, Notes.class);
                                        startActivity(intent);
                                        finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        Toast.makeText(MainActivity.this, "Unable to log in", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
        String userToken = sharedPref.getString("token", "default_value");
        if (!userToken.equals("default_value")) {
            Intent intent = new Intent(MainActivity.this, Notes.class);
            startActivity(intent);
            finish();
        }
    }
}
