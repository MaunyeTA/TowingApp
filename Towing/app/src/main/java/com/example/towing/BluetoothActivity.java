package com.example.towing;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("Second Activity created...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect);

        Toolbar toolbar = findViewById(R.id.bluetoothToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Paired Bluetooth devices");

        FillTheRecyclerView();

    }

    private void FillTheRecyclerView(){
        System.out.println("Recycler View Started");
        int devices = getIntent().getIntExtra("DeviceNum", 0);
        ArrayList<String> deviceNames = new ArrayList<>();
        if(devices > 0){
            for (int x=0; x<devices; x++){
                deviceNames.add(getIntent().getStringExtra("Device"+x));
            }
        }
        System.out.println("Recycler View Started 2");
        RecylerViewAdapter recylerViewAdapter = new RecylerViewAdapter(this, deviceNames);
        RecyclerView recyclerView = findViewById(R.id.recylerBluetooths);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recylerViewAdapter);

        System.out.println("Recycler View Started 3");
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
