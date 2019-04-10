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


public class Signup extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText password02;
    private final String SIGN_UP_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register";
    private Button signup;
    private Button cancel;
    private String token;

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
        cancel = findViewById(R.id.cancelButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstNameString = firstName.getText().toString().trim();
                final String lastNameString = lastName.getText().toString().trim();
                final String emailString = email.getText().toString().trim();
                final String passwordString = password.getText().toString().trim();
                String passwordString02 = password02.getText().toString().trim();

                if (firstNameString.equals("")) {
                    firstName.setError("Please enter a first name");
                    return;
                }

                if (lastNameString.equals("")) {
                    lastName.setError("Please enter a last name");
                    return;
                }
                if (emailString.equals("")) {
                    email.setError("Please enter an email");
                    return;
                }
                if (passwordString.length() < 6) {
                    password.setError("Minimun password length is 6");
                    return;
                }
                if (!passwordString.equals(passwordString02)) {
                    Toast.makeText(Signup.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
                    email.setError("Please enter a valid email");
                    return;
                }

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
                        Signup.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Signup.this, "Unable to display note", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String j = response.body().string();
                            try {
                                JSONObject root = new JSONObject(j);
                                if (root.has("token")) {
                                    token = root.getString("token");
                                    SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("token", token);
                                    editor.apply();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(Signup.this, Notes.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Signup.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Signup.this, "Unable to display note", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });


    }
}
