package com.example.inclass10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Signup extends AppCompatActivity {
    EditText firstName, lastName, email, password, password02;
    public  final String SIGN_UP_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTestEmail);
        password = findViewById(R.id.editTextPassword);
        password02 = findViewById(R.id.editTextPasswordCom);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SIGN_UP_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Demo", response.body().toString());

            }
        });

    }
}
