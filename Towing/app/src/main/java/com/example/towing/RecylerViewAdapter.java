package com.example.towing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {

    private ArrayList<String> deviceNames;
    private LayoutInflater layoutInflater;
    private Context context;
    public RecylerViewAdapter(Context context, ArrayList<String> devices) {
        this.layoutInflater = LayoutInflater.from(context);
        this.deviceNames = devices;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bluetooth_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(deviceNames.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("DeviceName", deviceNames.get(holder.getAdapterPosition()));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtDeviceName);
            linearLayout = itemView.findViewById(R.id.btDevice);
        }
    }
}
