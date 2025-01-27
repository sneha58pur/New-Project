package com.example.thirdyearproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.text.BreakIterator;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<ModelTeacher> data;
    private Context context;

    public CustomAdapter(ArrayList<ModelTeacher> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_view, parent, false); // Updated XML for individual items
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelTeacher modelTeacher = data.get(position);

        // Set the title and subtitle
        holder.title.setText(modelTeacher.getTitle());
        holder.subtitle.setText(modelTeacher.getSubtitle());
        holder.batch.setText(modelTeacher.getBatch());

        // Handle item click to open the PDF
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(modelTeacher.getPdfUrl()), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle item long click to open the pop-up menu
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.popup_menu);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.update) {
                    // Navigate to UpdateGuideActivity
                    Intent intent = new Intent(context, UpdateGuideActivity.class);
                    intent.putExtra("key", modelTeacher.getKey());
                    intent.putExtra("title", modelTeacher.getTitle());
                    intent.putExtra("subtitle", modelTeacher.getSubtitle());
                    intent.putExtra("pdfUrl", modelTeacher.getPdfUrl());
                    context.startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.delete) {
                    // Delete item from Firebase
                    FirebaseDatabase.getInstance().getReference("PDFs")
                            .child(modelTeacher.getKey())
                            .removeValue()
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    return true;
                }
                return false;
            });

            popupMenu.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle,batch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            batch = itemView.findViewById(R.id.batchInfo);
        }
    }
}
