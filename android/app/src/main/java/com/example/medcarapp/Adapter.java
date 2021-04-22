package com.example.medcarapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    String names[], desc[];
    Context context;
    int row_index = -1;

    public Adapter(Context ct, String s1[], String s2[]) {
        context = ct;
        names = s1;
        desc = s2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.carNames.setText(names[position]);
        holder.description.setText(desc[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.row_constraintlayout.setBackgroundColor(Color.parseColor("#add8e6"));
        }
        else
        {
            holder.row_constraintlayout.setBackgroundColor(Color.parseColor("#e7f4f3"));
        }

    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView carNames, description;
        ImageView carLogo;
        ConstraintLayout row_constraintlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carNames = itemView.findViewById(R.id.carNames);
            description = itemView.findViewById(R.id.description);
            carLogo = itemView.findViewById(R.id.carLogo);
            row_constraintlayout = (ConstraintLayout) itemView.findViewById(R.id.constraintrv);
        }
    }
}
