package com.example.inclass10;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button signUp;
    Button login;
    EditText email,password;
    public  final String LOGIN_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = findViewById(R.id.buttonSignupMain);
        login = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Signup.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email.getText().toString())
                        .add("password",password.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(LOGIN_URL)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();
                Log.d("demo",formBody.toString());

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("demo2222",response.toString());
                        String jsonData = response.body().string();
                        if (response.isSuccessful()){
                            Log.d("demo","sucess");
                        }
                    }
                });

            }
//            }
        });
    }
}
