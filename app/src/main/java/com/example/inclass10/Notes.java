package com.example.inclass10;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Notes extends AppCompatActivity {

    TextView editTextNote;
    FloatingActionButton logOff;
    RecyclerView recyclerView;
    Button addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editTextNote = findViewById(R.id.editTextNotes);
        logOff = findViewById(R.id.floatingActionButtonLogOff);
        recyclerView = findViewById(R.id.recycleView);
        addNote = findViewById(R.id.buttonAddNote);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextNote.getText().toString().equals("")){
                    editTextNote.setError("Please enter a note");
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                String string = sharedPref.getString("token", "default_value");
                Log.d("notes here", string);
            }
        });



    }
}
