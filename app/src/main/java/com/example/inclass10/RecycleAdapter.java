package com.example.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecycleAdapter extends RecyclerView.Adapter <RecycleAdapter.ViewHolder> {



    private ArrayList<NoteObject> arrayList;
    private UpdateAdapter updateAdapter;

    public RecycleAdapter(ArrayList<NoteObject> arrayList, UpdateAdapter updateAdapter) {
        this.updateAdapter = updateAdapter;
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
                SharedPreferences sharedPref = view.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
                String token = sharedPref.getString("token", "default_value");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(DELETE_NOTE_URL)
                        .header("x-access-token", token)
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.isSuccessful()){
                            updateAdapter.updateAdapter(i);
                        }
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
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayMessage = itemView.findViewById(R.id.textViewNote);
            deleteIcon = itemView.findViewById(R.id.imageButton);
        }
    }

    public interface UpdateAdapter{
        void updateAdapter(int index);
    }
}
