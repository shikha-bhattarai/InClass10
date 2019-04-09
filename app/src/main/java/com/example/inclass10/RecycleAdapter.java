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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {


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
                SharedPreferences sharedPref = view.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
                String string = sharedPref.getString("token", "default_value");
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


        }
    }
}
