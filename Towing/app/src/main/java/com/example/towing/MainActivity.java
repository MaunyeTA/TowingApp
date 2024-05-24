package com.example.towing;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothDevice selectedBluetoothDevice = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedReader br = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final int REQUEST_ENABLE_BT = 1;

    private boolean Connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Towing App");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(this, "Please Enable bluetooth permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN}, 2);
            }
        }

        BindConnect(getIntent().getStringExtra("DeviceName"));
        SetButtonsEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.btnConnectItem);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Do something when menu item 1 is clicked
                Connect();
                return true;
            }
        });

        MenuItem menuItem1 = menu.findItem(R.id.btnAboutItem);
        menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Do something when menu item 1 is clicked
                About();
                return true;
            }
        });

        return true;
    }

    private void About(){
        Intent intent1 = new Intent(this, AboutActivity.class);
        System.out.println("Acticity started......");
        this.startActivity(intent1);

    }

    private void Response(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EditText editText =  findViewById(R.id.editTextTextMultiLine);
                String strResponse = "";
                while(true){
                    if(br != null){
                        try {
                            strResponse = br.readLine();
                            editText.append("\n"+strResponse);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void BindConnect(String strDeviceName){



        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Toast.makeText(this, "BlueTooth is disabled", Toast.LENGTH_SHORT).show();
        } else {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN}, 2);
                return;
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            System.out.println(strDeviceName);
            ArrayList<String> devices = new ArrayList<>();
            for(BluetoothDevice bluetoothDevice : pairedDevices){
                if(bluetoothDevice.getName().equals(strDeviceName)){
                    selectedBluetoothDevice = bluetoothDevice;
                    try {
                        bluetoothSocket = selectedBluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                        bluetoothSocket.connect();
                        InputStream inputStream = bluetoothSocket.getInputStream();
                        OutputStream outputStream = bluetoothSocket.getOutputStream();
                        outputStreamWriter = new OutputStreamWriter(outputStream);
                        br = new BufferedReader(new InputStreamReader(inputStream));
                        Connected = true;
                        Toast.makeText(this, "Connected to device", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(this, "Could not connect to device", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    break;
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void SetButtonsEvents(){

        ImageButton imageButtonUp = findViewById(R.id.btnUp);
        ImageButton imageButtonDown = findViewById(R.id.btnDown);
        Response();

        imageButtonUp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(outputStreamWriter != null && br != null){
                            String string = "2";
                            try {
                                outputStreamWriter.write(string);
                                outputStreamWriter.flush();
                                return true;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }else{
                            return false;
                        }
                    case MotionEvent.ACTION_UP:
                        if(outputStreamWriter != null && br != null){
                            String string = "5";
                            try {
                                outputStreamWriter.write(string);
                                outputStreamWriter.flush();
                                return true;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }else{
                            return false;
                        }
                    default:
                        return false;
                }
            }
        });

        imageButtonDown.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(outputStreamWriter != null && br != null){
                            String string = "8";
                            try {
                                outputStreamWriter.write(string);
                                outputStreamWriter.flush();
                                return true;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }else{
                            return false;
                        }
                    case MotionEvent.ACTION_UP:
                        if(outputStreamWriter != null && br != null){
                            String string = "5";
                            try {
                                outputStreamWriter.write(string);
                                outputStreamWriter.flush();
                                return true;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }else{
                            return false;
                        }
                    default:
                        return false;
                }
            }
        });

    }

    public void Connect() {

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Toast.makeText(this, "BlueTooth is disabled", Toast.LENGTH_SHORT).show();
        } else {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(this, "Please Enable bluetooth permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN}, 2);
                return;
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            ArrayList<String> devices = new ArrayList<>();
            for(BluetoothDevice bluetoothDevice : pairedDevices){
                System.out.println(bluetoothDevice.getName());
                devices.add(bluetoothDevice.getName());
            }

            System.out.println("Devices found...");

            Intent intent = new Intent(this, BluetoothActivity.class);
            intent.putExtra("DeviceNum", devices.size());
            for (int x=0; x<devices.size(); x++){
                intent.putExtra("Device"+x, devices.get(x));
                System.out.println(devices.get(x));
            }
            this.startActivity(intent);
        }

    }
}