package com.example.medcarapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    String serverName[], serverDesc[];
    int images[];
    Context context;

    public ServerAdapter(Context ct, String name[], String desc[], int img[]) {
        context = ct;
        serverName = name;
        serverDesc = desc;
        images = img;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.server_rows, parent, false);
        return new ServerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServerAdapter.ViewHolder holder, int position) {
        holder.serverName.setText(serverName[position]);
        holder.serverDesc.setText(serverDesc[position]);
        holder.internetIcon.setImageResource(images[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(holder.getAdapterPosition()==0){
                    intent = new Intent(context, MainActivity.class);
                    intent.putExtra(context.getString(R.string.switchServer), false);
                } else if(holder.getAdapterPosition()==1){
                    intent = new Intent(context, MainActivity.class);
                    intent.putExtra(context.getString(R.string.switchServer), true);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serverName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView serverName, serverDesc;
        ImageView internetIcon;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            serverName = itemView.findViewById(R.id.serverNames);
            serverDesc = itemView.findViewById(R.id.serverDescription);
            internetIcon = itemView.findViewById(R.id.serverPic);
        }
    }
}
