package com.example.ifty.mynotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifty.mynotes.NewNoteActivity;
import com.example.ifty.mynotes.R;
import com.example.ifty.mynotes.models.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    ArrayList<Note>noteList;
    int layout;

    public NotesAdapter(Context context, ArrayList<Note> noteList, int layout) {
        this.context = context;
        this.noteList = noteList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new  NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        final Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.note.setText(note.getNote());
        holder.dateNtimeTv.setText(note.getDateNtime());
        if (note.getPhotoPath()!=null){
            holder.imageView.setVisibility(View.VISIBLE);
            Bitmap bmp = BitmapFactory.decodeFile(note.getPhotoPath());
            holder.imageView.setImageBitmap(bmp);
        }
        holder.titleNnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewNoteActivity.class);
                intent.putExtra("newOrViewNote","note view");
                intent.putExtra("id",note.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout titleNnote;
        TextView title, note, dateNtimeTv;
        ImageView imageView;

        public NotesViewHolder(View itemView) {
            super(itemView);
            titleNnote=itemView.findViewById(R.id.titleNnote);
            title=itemView.findViewById(R.id.titleTv);
            note=itemView.findViewById(R.id.noteTv);
            dateNtimeTv=itemView.findViewById(R.id.dateNtimeTv);
            imageView=itemView.findViewById(R.id.imageVW);
        }
    }


}
