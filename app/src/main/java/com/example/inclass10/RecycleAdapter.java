package com.example.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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

public class RecycleAdapter extends RecyclerView.Adapter <RecycleAdapter.ViewHolder> {



    ArrayList<NoteObject> arrayList;

    public RecycleAdapter(ArrayList<NoteObject> arrayList) {
        this.arrayList = arrayList;
    }

    static final String DISPLAY_NOTE_KEY = "display_key";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_recycle_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.displayMessage.setText(arrayList.get(i).getMessage());

        viewHolder.displayMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DisplayNote.class);
                intent.putExtra(DISPLAY_NOTE_KEY, arrayList.get(i).getMessageID());
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageId = arrayList.get(i).getMessageID();
                final String DELETE_NOTE_URL = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId="+messageId;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(DELETE_NOTE_URL)
                        .header("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYWJlMjAwZDQ2ZWQ3MGNmNDA5NzZiMSIsImlhdCI6MTU1NDgzMjUzNSwiZXhwIjoxNTU0OTE4OTM1fQ.GV7lcjkMMP6vuGEjhIWC3Br0CoHbgZh6IWsvMslC6uI")
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .build();

                Log.d("demo", DELETE_NOTE_URL);
                Log.d("demo2", request.toString());


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteIcon;
        TextView displayMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayMessage = itemView.findViewById(R.id.textViewNote);
            deleteIcon = itemView.findViewById(R.id.imageButton);
        }
    }
}
