package com.example.inclass10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Signup extends AppCompatActivity {
    EditText firstName, lastName, email, password, password02;
    public  final String SIGN_UP_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register";
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTestEmail);
        password = findViewById(R.id.editTextPassword);
        password02 = findViewById(R.id.editTextPasswordCom);
        signup = findViewById(R.id.buttonSignup);




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("name", firstName.getText().toString())
                        .add("email", email.getText().toString())
                        .add("password", password.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(SIGN_UP_URL)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Demo1110", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("Demo00", response.body().string());
                        Log.d("Demo00", "helo");
                    }
                });
            }
        });




    }
}
